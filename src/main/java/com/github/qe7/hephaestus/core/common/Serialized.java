package com.github.qe7.hephaestus.core.common;

import com.google.gson.JsonObject;

public interface Serialized {
    JsonObject serialize();

    void deserialize(JsonObject object);
}
