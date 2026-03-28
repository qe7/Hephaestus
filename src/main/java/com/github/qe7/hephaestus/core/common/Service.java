package com.github.qe7.hephaestus.core.common;

/**
 * A service is a component that can be loaded and used by the application.
 * It can be used to provide functionality, manage resources, or perform tasks.
 */
public interface Service {
    void load();

    default void unload() {
        // By default, services do not need to be unloaded.
    }
}
