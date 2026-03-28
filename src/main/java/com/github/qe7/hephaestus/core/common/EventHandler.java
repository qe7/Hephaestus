package com.github.qe7.hephaestus.core.common;

public interface EventHandler {
    /**
     * @return whether the event handler is listening for events
     */
    default boolean listening() {
        return true;
    }
}
