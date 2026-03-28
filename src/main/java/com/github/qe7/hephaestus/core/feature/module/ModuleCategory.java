package com.github.qe7.hephaestus.core.feature.module;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@RequiredArgsConstructor
@Getter
public enum ModuleCategory {
    COMBAT("Combat", new Color(255, 105, 97).getRGB()),
    MOVEMENT("Movement", new Color(255, 179, 71).getRGB()),
    RENDER("Render", new Color(174, 198, 207).getRGB()),
    MISC("Misc", new Color(255, 209, 220).getRGB()),
    CLIENT("Client", new Color(203, 153, 201).getRGB()),
    HUD("HUD", new Color(255, 255, 255).getRGB());

    private final String name;
    private final int color;
}
