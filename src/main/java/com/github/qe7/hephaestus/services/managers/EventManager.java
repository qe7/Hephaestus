package com.github.qe7.hephaestus.services.managers;

import com.github.qe7.hephaestus.core.common.EventHandler;
import com.github.qe7.hephaestus.core.common.Service;
import com.github.qe7.hephaestus.core.event.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages event listeners and event publishing. Supports registration of event handlers with annotated listener fields,
 * caching of listener templates for efficient registration, and publishing events to listeners in priority order.
 */
public final class EventManager implements Service {
    // @qe7
    // The EventManager and EventBus is ported from my old projects.

    private final Map<Class<? extends Event>, CopyOnWriteArrayList<ListenerWrapper>> listenerCache = new ConcurrentHashMap<>();
    private final Map<EventHandler, CopyOnWriteArrayList<ListenerWrapper>> handlerListeners = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<ListenerTemplate>> handlerRegistrationCache = new ConcurrentHashMap<>();

    @Override
    public void load() {
        // @qe7
        // No initialization needed for this implementation.

        // @qe7
        // I didn't think of this when writing the code,
        // but the handler registration cache could be pre-populated with templates for known eventHandler classes to avoid reflection on first registration.
        // I am also too lazy to implement that right now, but it would be a nice optimization I guess.
        // Also is pretty overkill for the current use case
    }

    /**
     * Publishes the given event to all registered listeners for its type.
     * Listeners are invoked in order of their priority. No per-publish sorting or copying.
     *
     * @param event the event instance to publish
     * @param <T>   the type of the event
     */
    public <T extends Event> void publishEvent(T event) {
        CopyOnWriteArrayList<ListenerWrapper> listeners = listenerCache.get(event.getClass());
        if (listeners == null) return;

        for (ListenerWrapper wrapper : listeners) {
            if (!wrapper.eventHandler.listening()) continue;

            @SuppressWarnings("unchecked") EventListener<T> listener = (EventListener<T>) wrapper.listener;
            listener.call(event);

            // @qe7
            // I debated whether to support event cancellation by checking if it's cancelled after each listener call.
            // Really depends on the use case, but for now I think it's simpler to just not support it and let listeners handle it themselves if needed.
            // Can be pank for cancelling events by one listener and having that affect other listeners, but I don't think we have any use cases for that right now,
            // and it can always be re-added later if needed.

            /*if (event instanceof CancellableEvent && ((CancellableEvent) event).isCancelled()) {
                break;
            }*/
        }
    }

    /**
     * Registers all event listener fields in the given eventHandler.
     * Discovers fields annotated with {@code EventSubscriber}, creates listener wrappers,
     * and adds them to the listener caches. Uses cached field templates if available.
     *
     * @param eventHandler the eventHandler instance to register listeners from
     */
    public void registerHandler(EventHandler eventHandler) {
        Class<?> handlerClass = eventHandler.getClass();
        List<ListenerTemplate> cachedTemplates = handlerRegistrationCache.get(handlerClass);
        if (cachedTemplates != null) {
            for (ListenerTemplate tpl : cachedTemplates) {
                try {
                    tpl.field.setAccessible(true);
                    @SuppressWarnings("unchecked") EventListener<? extends Event> listener = (EventListener<? extends Event>) tpl.field.get(eventHandler);
                    ListenerWrapper wrapper = new ListenerWrapper(eventHandler, listener, tpl.eventClass, tpl.priority);
                    addListenerToCache(wrapper);
                    handlerListeners.computeIfAbsent(eventHandler, k -> new CopyOnWriteArrayList<>()).add(wrapper);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to register listener from cached field " + tpl.field.getName(), e);
                }
            }
            return;
        }

        CopyOnWriteArrayList<ListenerWrapper> discovered = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<ListenerTemplate> templates = new CopyOnWriteArrayList<>();

        for (Field field : handlerClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(EventSubscriber.class)) continue;

            EventSubscriber annotation = field.getAnnotation(EventSubscriber.class);
            EventPriority priority = annotation.value();

            field.setAccessible(true);
            try {
                @SuppressWarnings("unchecked") EventListener<? extends Event> listener = (EventListener<? extends Event>) field.get(eventHandler);
                ParameterizedType type = (ParameterizedType) field.getGenericType();
                @SuppressWarnings("unchecked") Class<? extends Event> eventClass = (Class<? extends Event>) type.getActualTypeArguments()[0];

                ListenerWrapper wrapper = new ListenerWrapper(eventHandler, listener, eventClass, priority);
                discovered.add(wrapper);

                ListenerTemplate tpl = new ListenerTemplate(field, eventClass, priority);
                templates.add(tpl);

                addListenerToCache(wrapper);
                handlerListeners.computeIfAbsent(eventHandler, k -> new CopyOnWriteArrayList<>()).add(wrapper);

            } catch (Exception e) {
                throw new RuntimeException("Failed to register listener " + field.getName(), e);
            }
        }
        if (!templates.isEmpty()) {
            handlerRegistrationCache.put(handlerClass, templates);
        }
    }

    /**
     * Unregisters all listeners associated with the given eventHandler. Removes them from the listener caches.
     *
     * @param wrapper the eventHandler instance to unregister listeners from
     */
    private void addListenerToCache(ListenerWrapper wrapper) {
        CopyOnWriteArrayList<ListenerWrapper> listeners = listenerCache.computeIfAbsent(wrapper.eventClass, k -> new CopyOnWriteArrayList<>());
        int idx = findInsertIndex(listeners, wrapper.priority.getValue());
        listeners.add(idx, wrapper);
    }

    // Find insertion index for descending order by priority value (higher value first).
    private int findInsertIndex(CopyOnWriteArrayList<ListenerWrapper> list, int priorityValue) {
        int low = 0;
        int high = list.size();
        while (low < high) {
            int mid = (low + high) >>> 1;
            int midVal = list.get(mid).priority.getValue();
            if (midVal < priorityValue) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    /**
     * Wrapper for an event listener, storing the associated eventHandler, listener instance, event class, and priority.
     */
    private static class ListenerWrapper {
        public final EventHandler eventHandler;
        public final EventListener<? extends Event> listener;
        public final Class<? extends Event> eventClass;
        public final EventPriority priority;

        public ListenerWrapper(EventHandler eventHandler, EventListener<? extends Event> listener, Class<? extends Event> eventClass, EventPriority priority) {
            this.eventHandler = eventHandler;
            this.listener = listener;
            this.eventClass = eventClass;
            this.priority = priority;
        }
    }

    // Template cached per eventHandler class: stores the Field to read the listener from each instance.
    private static class ListenerTemplate {
        public final Field field;
        public final Class<? extends Event> eventClass;
        public final EventPriority priority;

        public ListenerTemplate(Field field, Class<? extends Event> eventClass, EventPriority priority) {
            this.field = field;
            this.eventClass = eventClass;
            this.priority = priority;
        }
    }
}
