package com.github.qe7.hephaestus.services.managers;

import com.github.qe7.hephaestus.Hephaestus;
import com.github.qe7.hephaestus.core.common.EventHandler;
import com.github.qe7.hephaestus.core.common.Service;
import com.github.qe7.hephaestus.core.event.EventListener;
import com.github.qe7.hephaestus.core.event.EventSubscriber;
import com.github.qe7.hephaestus.core.feature.module.AbstractModule;
import com.github.qe7.hephaestus.core.feature.module.ModuleCategory;
import com.github.qe7.hephaestus.events.KeyPressEvent;
import com.github.qe7.hephaestus.features.modules.client.HudModule;
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

        modules.add(new HudModule());

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

    /**
     * Gets a module by its class.
     *
     * @param clazz the class of the module to get
     * @return the module with the specified class
     */
    public AbstractModule get(Class<? extends AbstractModule> clazz) {
        AbstractModule module = this.modules.get(clazz);
        if (module == null) {
            throw new IllegalStateException("Module not registered: " + clazz.getSimpleName());
        }
        return module;
    }

    /**
     * Gets modules.
     */
    public List<AbstractModule> getModules() {
        return new LinkedList<>(this.modules.values());
    }

    /**
     * Gets modules from a category.
     *
     * @param category the category to get modules from
     * @return the modules from the specified category
     */
    public List<AbstractModule> getModulesFromCategory(ModuleCategory category) {
        List<AbstractModule> modules = new LinkedList<>();
        for (AbstractModule module : this.modules.values()) {
            if (module.getCategory() == category) {
                modules.add(module);
            }
        }
        return modules;
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
