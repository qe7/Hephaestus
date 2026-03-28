package com.github.qe7.hephaestus.features.modules.client;

import com.github.qe7.hephaestus.core.event.EventListener;
import com.github.qe7.hephaestus.core.event.EventSubscriber;
import com.github.qe7.hephaestus.core.feature.module.AbstractModule;
import com.github.qe7.hephaestus.core.feature.module.ModuleCategory;
import com.github.qe7.hephaestus.events.UpdateEvent;
import com.github.qe7.hephaestus.features.settings.BooleanSetting;

public final class TestModule extends AbstractModule {

    private final BooleanSetting testSetting = setting(new BooleanSetting("Test Setting", false));

    public TestModule(String name, String description, ModuleCategory category) {
        super(name, description, category);
    }

    @EventSubscriber
    private final EventListener<UpdateEvent> updateEventListener = event -> {
        System.out.println("Update event called " + testSetting.getName() + "=" + testSetting.getValue());

        this.testSetting.setValue(!this.testSetting.getValue());
    };
}
