package com.github.qe7.hephaestus.features.modules.client;

import com.github.qe7.hephaestus.Hephaestus;
import com.github.qe7.hephaestus.core.event.EventListener;
import com.github.qe7.hephaestus.core.event.EventSubscriber;
import com.github.qe7.hephaestus.core.feature.module.AbstractModule;
import com.github.qe7.hephaestus.core.feature.module.ModuleCategory;
import com.github.qe7.hephaestus.events.Render2DEvent;
import com.github.qe7.hephaestus.services.managers.ModuleManager;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.ScaledResolution;

import java.awt.*;

public final class HudModule extends AbstractModule {

    public HudModule() {
        super("HUD", "Displays information on the ui", ModuleCategory.CLIENT);

        this.setEnabled(true); // Set enabled by default - @qe7
        this.getVisible().setValue(false); // Not visible by default, it's always seen so why have it in the list? - @qe7
    }

    @EventSubscriber
    private final EventListener<Render2DEvent> render2DEventListener = event -> {
        // @qe7
        // A temporary HUD for now, will be redone in the future with the hud elements system.
        final FontRenderer fontRenderer = this.minecraft.fontRenderer;
        final ScaledResolution scaledResolution = event.getScaledResolution();

        final String watermark = "Hephaestus " + Hephaestus.getInstance().getVersion();
        fontRenderer.drawStringWithShadow(watermark,
                2,
                2,
                Color.WHITE.getRGB());

        final String buildInfo = "Build " + Hephaestus.getInstance().getBuildInfo();
        fontRenderer.drawStringWithShadow(buildInfo,
                scaledResolution.getScaledWidth() - fontRenderer.getStringWidth(buildInfo) - 2,
                scaledResolution.getScaledHeight() - (8) - 2,
                Color.WHITE.getRGB());

        int offset = 2;
        for (AbstractModule module : Hephaestus.getInstance().getServices().get(ModuleManager.class).getModules()) {
            if (!module.getVisible().getValue()) {
                continue;
            }
            String moduleSuffix = module.getSuffix() != null ? " " + module.getSuffix() : "";
            String moduleName = module.getName() + moduleSuffix;
            fontRenderer.drawStringWithShadow(moduleName,
                    scaledResolution.getScaledWidth() - fontRenderer.getStringWidth(moduleName) - 2,
                    offset,
                    module.getCategory().getColor());
            offset += 10;
        }
    };
}
