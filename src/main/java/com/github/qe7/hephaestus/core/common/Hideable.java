package com.github.qe7.hephaestus.core.common;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Interface for objects that can be hidden based on a condition.
 * Implementing classes can use the hide condition to determine whether they should be hidden or not.
 *
 * @param <S> the type of the implementing class, used for method chaining
 */
public interface Hideable<S> {
    /**
     * Gets the hide condition for the object. The object will be hidden if the condition evaluates to true.
     *
     * @return the condition that determines whether the object should be hidden, or null if no condition is set
     */
    Supplier<Boolean> getHideCondition();

    /**
     * Sets the hide condition for the object. The object will be hidden if the condition evaluates to true.
     *
     * @param condition the condition that determines whether the object should be hidden
     */
    void setHideCondition(Supplier<Boolean> condition);

    S self();

    /**
     * Determines whether the object should be hidden based on the hide condition.
     * If the hide condition is not set (i.e., null), it returns false, meaning the object should not be hidden.
     * If the hide condition is set, it evaluates the condition and returns its result.
     *
     * @return true if the object should be hidden, false otherwise
     */
    default boolean shouldHide() {
        Supplier<Boolean> condition = getHideCondition();
        return condition != null && condition.get();
    }

    /**
     * Sets the hide condition for the object. The object will be hidden if the condition evaluates to true.
     * The condition must not be null; if it is, a NullPointerException will be thrown.
     *
     * @param condition the condition that determines whether the object should be hidden
     * @return the current instance for method chaining
     */
    default S supplyIf(Supplier<Boolean> condition) {
        setHideCondition(Objects.requireNonNull(condition, "condition"));
        return self();
    }
}
