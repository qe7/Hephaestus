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
import com.github.qe7.hephaestus.features.modules.combat.AntiKnockbackModule;
import com.github.qe7.hephaestus.features.settings.KeybindSetting;
import com.github.qe7.hephaestus.utils.FileUtil;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * The module manager is responsible for managing all modules, including loading and unloading them, as well as saving and loading their states and settings states to a default config.
 */
public final class ModuleManager implements Service, EventHandler {
    private final HashMap<Class<? extends AbstractModule>, AbstractModule> modules = new HashMap<>();

    private final String defaultConfigLocation = "default";

    /**
     * Loads the module manager, which will load all modules and their states and settings states from a default config.
     */
    @Override
    public void load() {
        System.out.println("Loading module manager...");

        List<AbstractModule> modules = new LinkedList<>();

        modules.add(new HudModule());
        modules.add(new AntiKnockbackModule());

        modules.forEach(module -> {
            this.modules.put(module.getClass(), module);
            Hephaestus.getInstance().getServices().get(EventManager.class).registerHandler(module);
        });

        System.out.println("Loaded " + this.modules.size() + " modules. (expected " + modules.size() + ")");

        System.out.println("Loading default module config...");

        String configString = FileUtil.readFile(defaultConfigLocation);
        if (configString == null) {
            System.out.println("No default module config found, skipping...");
        } else {
            final JsonObject config = FileUtil.GSON.fromJson(configString, JsonObject.class);

            for (AbstractModule module : this.modules.values()) {
                if (config.has(module.getName())) {
                    module.deserialize(config.getAsJsonObject(module.getName()));
                }
            }
            System.out.println("Default module config loaded.");
        }

        Hephaestus.getInstance().getServices().get(EventManager.class).registerHandler(this);
        System.out.println("Module manager loaded.");
    }

    /**
     * Unloads the module manager, which will save the configuration of all modules to a default config.
     */
    @Override
    public void unload() {
        System.out.println("Unloading module manager...");

        System.out.println("Saving default module config...");
        final JsonObject config = new JsonObject();

        for (AbstractModule module : this.modules.values()) {
            config.add(module.getName(), module.serialize());
        }

        FileUtil.writeFile(defaultConfigLocation, config.toString());
        if (FileUtil.readFile(defaultConfigLocation) == null) {
            System.err.println("Failed to save default module config.");
        } else {
            System.out.println("Default module config saved.");
        }

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
