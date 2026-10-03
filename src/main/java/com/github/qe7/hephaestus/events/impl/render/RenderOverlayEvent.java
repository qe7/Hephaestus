package com.github.qe7.hephaestus.events.impl.render;

import com.github.qe7.hephaestus.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.src.ScaledResolution;

@AllArgsConstructor
@Getter
public final class RenderOverlayEvent implements Event {
    private final ScaledResolution scaledResolution;
    private final float partialTicks;
}
