package com.github.qe7.hephaestus.events.impl.input;

import com.github.qe7.hephaestus.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class KeyPressEvent implements Event {
    private final int keyCode;
}
