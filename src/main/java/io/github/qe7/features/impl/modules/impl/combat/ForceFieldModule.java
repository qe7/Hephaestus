package io.github.qe7.features.impl.modules.impl.combat;

import io.github.qe7.Hephaestus;
import io.github.qe7.events.UpdateEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.features.impl.modules.api.settings.impl.DoubleSetting;
import io.github.qe7.utils.PacketUtil;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet7UseEntity;

import java.util.ArrayList;
import java.util.List;

public final class ForceFieldModule extends Module {

    private final BooleanSetting swing = new BooleanSetting("Swing", true);

    private final DoubleSetting range = new DoubleSetting("Range", 4.2, 0.0, 10.0, 0.1);

    private final List<Entity> targets = new ArrayList<>();

    public ForceFieldModule() {
        super("ForceField", "Automatically attacks entities in a given radius.", ModuleCategory.COMBAT);
    }

    @Subscribe
    public final Listener<UpdateEvent> onUpdate = new Listener<>(event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null || mc.theWorld == null) return;

        handleTargets();

        if (targets.isEmpty()) return;

        targets.forEach(this::handleAttack);
    });

    private void handleTargets() {
        final Minecraft mc = Minecraft.getMinecraft();
        targets.clear();
        mc.theWorld.loadedEntityList.stream().filter(entity -> entity != mc.thePlayer && entity instanceof EntityLiving && entity.isEntityAlive() && mc.thePlayer.getDistanceToEntity(entity) <= range.getValue()).forEach(targets::add);
    }

    private void handleAttack(Entity target) {
        final Minecraft mc = Minecraft.getMinecraft();

        if (target instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) target;
            if (Hephaestus.getInstance().getRelationManager().isFriend(player.username)) {
                return;
            }
        }

        if (swing.getValue()) {
            mc.thePlayer.swingItem();
        }
        PacketUtil.sendPacket(new Packet7UseEntity(mc.thePlayer.entityId, target.entityId, 1));
    }
}
