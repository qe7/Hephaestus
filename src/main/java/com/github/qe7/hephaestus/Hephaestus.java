package com.github.qe7.hephaestus;

import com.github.qe7.hephaestus.core.common.ServiceRegistry;
import com.github.qe7.hephaestus.services.managers.CommandManager;
import com.github.qe7.hephaestus.services.managers.EventManager;
import com.github.qe7.hephaestus.services.managers.ModuleManager;
import lombok.Getter;

/**
 * Welcome to the recode. (>_<)
 * By qe7/Shae
 */
@Getter
public final class Hephaestus {
    @Getter
    private static Hephaestus instance;

    private final String name, version, buildInfo;

    private final ServiceRegistry services = new ServiceRegistry();

    /**
     * Welcome to the recode. (>_<)
     */
    public Hephaestus() {
        instance = this;

        this.name = "Hephaestus";
        this.version = "2.0.0";
        this.buildInfo = String.format("%s.%s", BuildConstants.GIT_REVISION, BuildConstants.GIT_COMMIT);

        System.out.printf("%s %s (build %s) starting...", name, version, buildInfo);

        services.register(EventManager.class, new EventManager());
        services.register(ModuleManager.class, new ModuleManager());
        services.register(CommandManager.class, new CommandManager());

        System.out.println("Loading services...");
        services.loadAll();

        System.out.println("Registering shutdown hook...");
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    /**
     * Welcome to the recode. (>_<)
     */
    public void stop() {
        System.out.printf("%s %s stopping...", name, version);

        System.out.println("Unloading services...");
        services.unloadAll();
    }
}
