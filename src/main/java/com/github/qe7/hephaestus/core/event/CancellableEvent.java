package com.github.qe7.hephaestus.core.event;

/**
 * An event that can be cancelled
 */
public interface CancellableEvent extends Event {

    boolean isCancelled();

    void setCancelled(boolean cancelled);
}