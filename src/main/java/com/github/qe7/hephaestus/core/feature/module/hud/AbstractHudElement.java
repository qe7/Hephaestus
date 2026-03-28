package com.github.qe7.hephaestus.core.feature.module.hud;

import com.github.qe7.hephaestus.core.feature.module.AbstractModule;
import com.github.qe7.hephaestus.core.feature.module.ModuleCategory;

/**
 * Base class for HUD elements.
 */
public abstract class AbstractHudElement extends AbstractModule {

    // @qe7
    // TODO: Add properties for position, size, and other relevant attributes for HUD elements.
    public AbstractHudElement(String name, String description) {
        super(name, description, ModuleCategory.HUD);
    }
}
