package com.github.qe7.hephaestus.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ModuleCategory {
    COMBAT("Combat", 0xDE1A1A),
    MOVEMENT("Movement", 0xE8EBF7),
    MISC("Misc", 0xACBED8),
    RENDER("Render", 0xF2D398),
    EXPLOIT("Exploit", 0xD78521);

    private final String name;
    private final int color;
}
