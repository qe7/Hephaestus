package com.github.qe7.hephaestus.events;

import com.github.qe7.hephaestus.core.event.Event;
import lombok.Getter;

@Getter
public final class KeyPressEvent implements Event {
    private final int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }
}
