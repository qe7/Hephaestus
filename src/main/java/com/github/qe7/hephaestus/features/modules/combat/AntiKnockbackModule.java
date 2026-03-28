package com.github.qe7.hephaestus.features.modules.combat;

import com.github.qe7.hephaestus.core.event.EventListener;
import com.github.qe7.hephaestus.core.event.EventSubscriber;
import com.github.qe7.hephaestus.core.feature.module.AbstractModule;
import com.github.qe7.hephaestus.core.feature.module.ModuleCategory;
import com.github.qe7.hephaestus.events.PacketEvent;
import net.minecraft.src.Packet28EntityVelocity;

public final class AntiKnockbackModule extends AbstractModule {

    public AntiKnockbackModule() {
        super("Anti Knockback", "Removes knockback given to the player when hit", ModuleCategory.COMBAT);
    }

    @EventSubscriber
    private final EventListener<PacketEvent> packetEventListener = event -> {
        if (!event.isIncoming()) {
            return;
        }

        if (event.getPacket() instanceof Packet28EntityVelocity && this.minecraft.thePlayer != null) {
            Packet28EntityVelocity packet = (Packet28EntityVelocity) event.getPacket();
            if (packet.entityId == this.minecraft.thePlayer.entityId) {
                event.setCancelled(true);
            }
        }
    };
}
