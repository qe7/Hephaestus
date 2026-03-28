package com.github.qe7.hephaestus.events;

import com.github.qe7.hephaestus.core.event.CancellableEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.src.Packet;

@RequiredArgsConstructor
public final class PacketEvent implements CancellableEvent {

    @Getter
    private final Packet packet;

    private final Type type;

    private boolean cancelled;

    public boolean isIncoming() {
        return type == Type.INCOMING;
    }

    public boolean isOutgoing() {
        return type == Type.OUTGOING;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public enum Type {
        INCOMING,
        OUTGOING
    }
}
