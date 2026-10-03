package com.github.qe7.hephaestus.events;

public interface Listener<T extends Event> {
    void call(T event);
}