package com.github.qe7.hephaestus.core.event;

/**
 * Event listener interface. Implement this to listen for events.
 * Note that the event bus will not automatically register listeners. You must register them yourself.
 *
 * @param <T> the type of event to listen for
 */
public interface EventListener<T extends Event> {
    void call(T event);
}
