package com.github.qe7.hephaestus.modules;

import com.github.qe7.hephaestus.core.Globals;
import com.github.qe7.hephaestus.events.EventHandler;
import com.github.qe7.hephaestus.modules.impl.render.HUDModule;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class ModuleManager implements EventHandler {
    @Getter
    private final HashMap<Class<? extends AbstractModule>, AbstractModule> modules = new HashMap<>();

    public ModuleManager() {
        registerModule(new HUDModule());
    }

    private void registerModule(AbstractModule module) {
        modules.put(module.getClass(), module);
        Globals.EVENT_MANAGER.registerHandler(module);
    }

    public <T extends AbstractModule> T getModule(Class<T> moduleClass) {
        return moduleClass.cast(modules.get(moduleClass));
    }

    public List<AbstractModule> getEnabledModules() {
        return modules.values().stream().filter(AbstractModule::isEnabled).collect(Collectors.toList());
    }

    public List<AbstractModule> getModulesForCategory(ModuleCategory category) {
        return modules.values().stream().filter(module -> module.getCategory() == category).collect(Collectors.toList());
    }
}
