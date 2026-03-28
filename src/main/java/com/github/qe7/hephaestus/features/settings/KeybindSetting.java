package com.github.qe7.hephaestus.features.settings;

import com.github.qe7.hephaestus.core.feature.setting.AbstractSetting;
import com.google.gson.JsonObject;

public final class KeybindSetting extends AbstractSetting<Integer> {

    public KeybindSetting(String name, Integer defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        object.addProperty(getName(), getValue());
        return object;
    }

    @Override
    public void deserialize(JsonObject object) {
        if (object.has(getName())) {
            setValue(object.get(getName()).getAsInt());
        }
    }
}
