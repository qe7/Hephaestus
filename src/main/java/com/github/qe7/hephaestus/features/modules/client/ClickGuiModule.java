package com.github.qe7.hephaestus.features.modules.client;

import com.github.qe7.hephaestus.core.event.EventListener;
import com.github.qe7.hephaestus.core.event.EventSubscriber;
import com.github.qe7.hephaestus.core.feature.module.AbstractModule;
import com.github.qe7.hephaestus.core.feature.module.ModuleCategory;
import com.github.qe7.hephaestus.events.UpdateEvent;
import org.lwjgl.input.Keyboard;

public final class ClickGuiModule extends AbstractModule {

    public ClickGuiModule() {
        super("Click GUI", "Displays a clickable interface to manage modules and their settings", ModuleCategory.CLIENT);

        this.getKeybind().setValue(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventSubscriber
    private final EventListener<UpdateEvent> updateEventListener = event -> {

    };
}
