package com.github.qe7.hephaestus.events;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface EventSubscriber {
    EventPriority value() default EventPriority.NORMAL;
}