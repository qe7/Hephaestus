package com.github.qe7.hephaestus.core.common;

import java.util.*;

/**
 * A registry for managing service instances. Services can be registered, retrieved, loaded, and unloaded in a controlled manner.
 * Each service type can only be registered once, and services are loaded and unloaded in the order they were registered.
 */
public final class ServiceRegistry {
    private final Map<Class<?>, Service> services = new LinkedHashMap<>();

    /**
     * Registers a service instance with its class type. Each service type can only be registered once.
     *
     * @param type    the class type of the service to register
     * @param service the service instance to register
     * @param <T>     the type of the service
     * @throws IllegalStateException if a service is already registered for the given type
     */
    public <T extends Service> void register(Class<T> type, T service) {
        if (services.containsKey(type)) {
            throw new IllegalStateException("Already registered: " + type.getSimpleName());
        }
        services.put(type, service);
    }

    /**
     * Retrieves a registered service by its class type.
     *
     * @param type the class type of the service to retrieve
     * @param <T>  the type of the service
     * @return the registered service instance
     * @throws IllegalStateException if no service is registered for the given type
     */
    @SuppressWarnings("unchecked")
    public <T extends Service> T get(Class<T> type) {
        T service = (T) services.get(type);
        if (service == null) {
            throw new IllegalStateException("Not registered: " + type.getSimpleName());
        }
        return service;
    }

    /**
     * Loads all registered services in the order they were registered. Services should be loaded before use.
     */
    public void loadAll() {
        services.values().forEach(Service::load);
    }

    /**
     * Unloads all registered services in the reverse order they were registered. Services should be unloaded when no longer needed.
     */
    public void unloadAll() {
        List<Service> reversed = new ArrayList<>(services.values());
        Collections.reverse(reversed);
        reversed.forEach(Service::unload);
    }
}
