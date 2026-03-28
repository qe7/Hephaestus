package com.github.qe7.hephaestus.services.managers;

import com.github.qe7.hephaestus.Hephaestus;
import com.github.qe7.hephaestus.core.common.EventHandler;
import com.github.qe7.hephaestus.core.common.Service;
import com.github.qe7.hephaestus.core.event.EventListener;
import com.github.qe7.hephaestus.core.event.EventSubscriber;
import com.github.qe7.hephaestus.core.feature.module.AbstractModule;
import com.github.qe7.hephaestus.core.feature.module.ModuleCategory;
import com.github.qe7.hephaestus.events.KeyPressEvent;
import com.github.qe7.hephaestus.features.modules.client.TestModule;
import com.github.qe7.hephaestus.features.settings.KeybindSetting;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * The module manager is responsible for managing all modules, including loading and unloading them, as well as saving and loading their states and settings states to a default config.
 */
public final class ModuleManager implements Service, EventHandler {
    private final HashMap<Class<? extends AbstractModule>, AbstractModule> modules = new HashMap<>();

    /**
     * Loads the module manager, which will load all modules and their states and settings states from a default config.
     */
    @Override
    public void load() {
        System.out.println("Loading module manager...");

        List<AbstractModule> modules = new LinkedList<>();

        modules.add(new TestModule("Test Module", "A test module for testing purposes.", ModuleCategory.CLIENT));

        modules.forEach(module -> {
            this.modules.put(module.getClass(), module);
            Hephaestus.getInstance().getServices().get(EventManager.class).registerHandler(module);
        });

        System.out.println("Loaded " + this.modules.size() + " modules. (expected " + modules.size() + ")");

        Hephaestus.getInstance().getServices().get(EventManager.class).registerHandler(this);
        System.out.println("Module manager loaded.");
    }

    /**
     * Unloads the module manager, which will save the configuration of all modules to a default config.
     */
    @Override
    public void unload() {
        System.out.println("Unloading module manager...");

        // @qe7
        // TODO: Save module states and settings states to a default config here.

        System.out.println("Module manager unloaded.");
    }

    @EventSubscriber
    private final EventListener<KeyPressEvent> keyPressEventListener = event -> {
        for (AbstractModule module : this.modules.values()) {
            KeybindSetting keyBind = module.getKeybind();
            if (keyBind.getValue() == event.getKey()) {
                module.setEnabled(!module.isEnabled());
            }
        }
    };
}
