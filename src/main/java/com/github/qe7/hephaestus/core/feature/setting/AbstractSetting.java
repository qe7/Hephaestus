package com.github.qe7.hephaestus.core.feature.setting;

import com.github.qe7.hephaestus.core.common.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Represents a setting that can be configured by the user.
 * Settings can have a default value and a current value.
 * They can also be hidden based on certain conditions.
 *
 * @param <T> The type of the setting's value.
 */
@Getter
@Setter
public abstract class AbstractSetting<T> implements Serialized, Displayable, ChildNode<SettingParent>, Hideable<AbstractSetting<T>>, SettingParent {

    private final ChildRegistry<SettingParent, AbstractSetting<?>> childRegistry = new ChildRegistry<>(this);

    private final String name;

    private final T defaultValue;
    private T value;

    private SettingParent parent;

    private Supplier<Boolean> hideCondition = () -> false;

    public AbstractSetting(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    /**
     * Adds a child setting to this setting.
     *
     * @param child The child setting to add.
     * @param <S>   The type of the child setting's value.
     * @return The child setting that was added.
     */
    @Override
    public <S extends AbstractSetting<?>> S setting(S child) {
        return childRegistry.add(child);
    }

    /**
     * Gets the parent of this setting, if it exists.
     *
     * @return An Optional containing the parent setting, or an empty Optional if this setting has no parent.
     */
    @Override
    public Optional<SettingParent> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * Attaches this setting to a parent setting.
     *
     * @param parent The parent setting to attach to.
     */
    @Override
    public void attachTo(SettingParent parent) {
        this.parent = Objects.requireNonNull(parent, "parent");
    }

    /**
     * Gets the children of this setting.
     *
     * @return An unmodifiable list of child settings.
     */
    @Override
    public List<AbstractSetting<?>> getChildren() {
        return Collections.unmodifiableList(childRegistry.view());
    }

    /**
     * Gets the children of this setting.
     *
     * @return A list of child settings.
     */
    @Override
    public AbstractSetting<T> self() {
        return this;
    }
}
