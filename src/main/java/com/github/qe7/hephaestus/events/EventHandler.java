package com.github.qe7.hephaestus.events;

public interface EventHandler {
    default boolean listening() {
        return true;
    }
}