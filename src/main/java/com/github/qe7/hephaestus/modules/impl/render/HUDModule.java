package com.github.qe7.hephaestus.modules.impl.render;

import com.github.qe7.hephaestus.core.Globals;
import com.github.qe7.hephaestus.events.EventSubscriber;
import com.github.qe7.hephaestus.events.Listener;
import com.github.qe7.hephaestus.events.impl.render.RenderOverlayEvent;
import com.github.qe7.hephaestus.modules.AbstractModule;
import com.github.qe7.hephaestus.modules.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;

import java.awt.*;

public final class HUDModule extends AbstractModule {
    private final Color outlineColor = new Color(0, 0, 0, 100);

    public HUDModule() {
        super("HUD", "Displays the HUD", ModuleCategory.RENDER);

        this.setEnabled(true);
    }

    @EventSubscriber
    private final Listener<RenderOverlayEvent> renderOverlayListener = event -> {
        final FontRenderer fontRenderer = Minecraft.get().fontRenderer;

        this.drawStringWithOutline(fontRenderer, String.format("%s %s", Globals.NAME, Globals.VERSION_FULL), 2, 2, -1);

        for (AbstractModule module : Globals.MODULE_MANAGER.getEnabledModules()) {
            final String moduleName = module.getName();
            final int x = 2;
            final int y = 2 + (Globals.MODULE_MANAGER.getEnabledModules().indexOf(module) + 1) * (8 + 2);
            this.drawStringWithOutline(fontRenderer, moduleName, x, y, -1);
        }
    };

    private void drawStringWithOutline(FontRenderer fontRenderer, String text, int x, int y, int color) {
        fontRenderer.drawString(text, x - 1, y, outlineColor.getRGB());
        fontRenderer.drawString(text, x + 1, y, outlineColor.getRGB());
        fontRenderer.drawString(text, x, y - 1, outlineColor.getRGB());
        fontRenderer.drawString(text, x, y + 1, outlineColor.getRGB());
        fontRenderer.drawString(text, x, y, color);
    }
}
