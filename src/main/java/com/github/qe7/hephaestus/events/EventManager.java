package com.github.qe7.hephaestus.events;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// @Shae
// Probably been optimized and overengineered to the point of it being unholy, but it works.
public final class EventManager {
    private final Map<Class<? extends Event>, CopyOnWriteArrayList<ListenerWrapper>> listenerCache = new ConcurrentHashMap<>();
    private final Map<EventHandler, CopyOnWriteArrayList<ListenerWrapper>> handlerListeners = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<ListenerTemplate>> handlerRegistrationCache = new ConcurrentHashMap<>();

    public <T extends Event> void publishEvent(final T event) {
        // @Shae
        // Fast-path exact class listeners.
        final CopyOnWriteArrayList<ListenerWrapper> direct = this.listenerCache.get(event.getClass());
        if (direct != null && !direct.isEmpty()) {
            for (final ListenerWrapper wrapper : direct) {
                if (!wrapper.eventHandler.listening()) continue;
                @SuppressWarnings("unchecked") final Listener<T> eventListener = (Listener<T>) wrapper.listener;
                eventListener.call(event);
            }
            return;
        }

        // @Shae
        // Fallback to supertypes and interfaces.
        final List<ListenerWrapper> combined = new ArrayList<>();
        for (final Class<?> key : this.getAssignableKeys(event.getClass())) {
            @SuppressWarnings("unchecked")
            final CopyOnWriteArrayList<ListenerWrapper> list = this.listenerCache.get((Class<? extends Event>) key);
            if (list != null && !list.isEmpty()) combined.addAll(list);
        }

        if (combined.isEmpty()) return;

        // @Shae
        // Deduplicate before priority sorting.
        final Set<ListenerWrapper> seen = new LinkedHashSet<>(combined);
        combined.clear();
        combined.addAll(seen);

        // @Shae
        // Highest priority runs first.
        combined.sort((a, b) -> Integer.compare(b.priority.getValue(), a.priority.getValue()));

        for (final ListenerWrapper wrapper : combined) {
            if (!wrapper.eventHandler.listening()) continue;
            @SuppressWarnings("unchecked") final Listener<T> eventListener = (Listener<T>) wrapper.listener;
            eventListener.call(event);
        }
    }

    /**
     * Collect listenerCache keys that are assignable from the given runtime class.
     * This includes the concrete class, its superclasses and implemented interfaces.
     */
    private Set<Class<?>> getAssignableKeys(final Class<?> runtimeClass) {
        final Set<Class<?>> result = new LinkedHashSet<>();
        for (Class<?> cls = runtimeClass; cls != null && cls != Object.class; cls = cls.getSuperclass()) {
            if (this.listenerCache.containsKey(cls)) result.add(cls);
            this.collectInterfacesAssignableFrom(cls, result);
        }
        return result;
    }

    private void collectInterfacesAssignableFrom(final Class<?> cls, final Set<Class<?>> out) {
        for (final Class<?> iface : cls.getInterfaces()) {
            if (this.listenerCache.containsKey(iface)) out.add(iface);
            this.collectInterfacesAssignableFrom(iface, out);
        }
    }

    public void registerHandler(final EventHandler eventHandler) {
        // @Shae
        // Reflection path for new eventHandler instances.
        final Class<?> handlerClass = eventHandler.getClass();
        final List<ListenerTemplate> cachedTemplates = this.handlerRegistrationCache.get(handlerClass);
        if (cachedTemplates != null) {
            for (final ListenerTemplate tpl : cachedTemplates) {
                try {
                    tpl.field.setAccessible(true);
                    final Listener<? extends Event> eventListener = (Listener<? extends Event>) tpl.field.get(eventHandler);
                    final ListenerWrapper wrapper = new ListenerWrapper(eventHandler, eventListener, tpl.eventClass, tpl.priority);
                    this.addListenerToCache(wrapper);
                    this.handlerListeners.computeIfAbsent(eventHandler, k -> new CopyOnWriteArrayList<>()).add(wrapper);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to register listener from cached field " + tpl.field.getName(), e);
                }
            }
            return;
        }

        final CopyOnWriteArrayList<ListenerTemplate> templates = new CopyOnWriteArrayList<>();

        for (final Field field : handlerClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(EventSubscriber.class)) continue;

            final EventSubscriber annotation = field.getAnnotation(EventSubscriber.class);
            final EventPriority priority = annotation.value();

            field.setAccessible(true);
            try {
                final Listener<? extends Event> eventListener = (Listener<? extends Event>) field.get(eventHandler);
                final ParameterizedType type = (ParameterizedType) field.getGenericType();
                @SuppressWarnings("unchecked") Class<? extends Event> eventClass = (Class<? extends Event>) type.getActualTypeArguments()[0];

                final ListenerWrapper wrapper = new ListenerWrapper(eventHandler, eventListener, eventClass, priority);

                final ListenerTemplate tpl = new ListenerTemplate(field, eventClass, priority);
                templates.add(tpl);

                this.addListenerToCache(wrapper);
                this.handlerListeners.computeIfAbsent(eventHandler, k -> new CopyOnWriteArrayList<>()).add(wrapper);

            } catch (Exception e) {
                throw new RuntimeException("Failed to register listener " + field.getName(), e);
            }
        }
        if (!templates.isEmpty()) {
            this.handlerRegistrationCache.put(handlerClass, templates);
        }
    }

    private void addListenerToCache(final ListenerWrapper wrapper) {
        final CopyOnWriteArrayList<ListenerWrapper> listeners = this.listenerCache.computeIfAbsent(wrapper.eventClass, k -> new CopyOnWriteArrayList<>());
        final int idx = this.findInsertIndex(listeners, wrapper.priority.getValue());
        listeners.add(idx, wrapper);
    }

    private int findInsertIndex(final CopyOnWriteArrayList<ListenerWrapper> list, final int priorityValue) {
        int low = 0;
        int high = list.size();
        while (low < high) {
            final int mid = (low + high) >>> 1;
            final int midVal = list.get(mid).priority.getValue();
            if (midVal < priorityValue) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private static final class ListenerWrapper {
        public final EventHandler eventHandler;
        public final Listener<? extends Event> listener;
        public final Class<? extends Event> eventClass;
        public final EventPriority priority;

        public ListenerWrapper(final EventHandler eventHandler, final Listener<? extends Event> listener,
                               final Class<? extends Event> eventClass, final EventPriority priority) {
            this.eventHandler = eventHandler;
            this.listener = listener;
            this.eventClass = eventClass;
            this.priority = priority;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final ListenerWrapper that = (ListenerWrapper) o;
            return Objects.equals(eventHandler, that.eventHandler) &&
                    Objects.equals(listener, that.listener) &&
                    Objects.equals(eventClass, that.eventClass) &&
                    Objects.equals(priority, that.priority);
        }

        @Override
        public int hashCode() {
            return Objects.hash(eventHandler, listener, eventClass, priority);
        }
    }

    private static final class ListenerTemplate {
        public final Field field;
        public final Class<? extends Event> eventClass;
        public final EventPriority priority;

        public ListenerTemplate(final Field field, final Class<? extends Event> eventClass, final EventPriority priority) {
            this.field = field;
            this.eventClass = eventClass;
            this.priority = priority;
        }
    }
}