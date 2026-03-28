package com.github.qe7.hephaestus.events;

import com.github.qe7.hephaestus.core.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.src.ScaledResolution;

@AllArgsConstructor
@Getter
public final class Render2DEvent implements Event {

    private final ScaledResolution scaledResolution;
}
