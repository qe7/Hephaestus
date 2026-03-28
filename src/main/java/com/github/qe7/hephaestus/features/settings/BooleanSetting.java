package com.github.qe7.hephaestus.features.settings;

import com.github.qe7.hephaestus.core.feature.setting.AbstractSetting;

public final class BooleanSetting extends AbstractSetting<Boolean> {

    public BooleanSetting(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }
}
