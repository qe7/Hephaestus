package com.github.qe7.hephaestus.core.feature.module;

import com.github.qe7.hephaestus.core.common.*;
import com.github.qe7.hephaestus.core.feature.command.AbstractCommand;
import com.github.qe7.hephaestus.core.feature.setting.AbstractSetting;
import com.github.qe7.hephaestus.features.settings.BooleanSetting;
import com.github.qe7.hephaestus.features.settings.KeybindSetting;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Keyboard;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents a module/feature in the client.
 * Modules can be toggled on/off and can have settings.
 */
@Getter
@Setter
public abstract class AbstractModule extends AbstractCommand implements EventHandler, Serialized, Displayable, ParentNode<AbstractSetting<?>>, SettingParent {
    private final ChildRegistry<SettingParent, AbstractSetting<?>> settings = new ChildRegistry<>(this);

    private final String description;

    private final ModuleCategory category;

    private String suffix = "";

    private boolean enabled = false;

    private final BooleanSetting visible = setting(new BooleanSetting("Visible", true));
    private final KeybindSetting keybind = setting(new KeybindSetting("Keybind", Keyboard.KEY_G));

    /**
     * Creates a new module with the given name, description, and category.
     *
     * @param name        The name of the module (e.g. "KillAura").
     * @param description A brief description of the module's functionality, which can be displayed in the GUI (e.g. "Automatically attacks nearby entities").
     * @param category    The category that this module belongs to (e.g. Combat, Movement, Render, etc.). This can be used for organizational purposes in the GUI.
     */
    public AbstractModule(String name, String description, ModuleCategory category) {
        super(name);
        this.description = description;
        this.category = category;
    }

    /**
     * Called when the module is enabled. Override this method to add custom behavior when the module is toggled on.
     */
    public void onEnable() {
    }

    /**
     * Called when the module is disabled. Override this method to add custom behavior when the module is toggled off.
     */
    public void onDisable() {
    }

    /**
     * Toggles the module on/off. If the module is being enabled, onEnable() will be called. If the module is being disabled, onDisable() will be called.
     *
     * @param enabled Whether the module should be enabled or disabled.
     */
    public void setEnabled(boolean enabled) {
        if (this.isEnabled() == enabled) {
            return;
        }

        this.enabled = enabled;

        if (enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    /**
     * Determines whether the module should listen for events.
     * By default, this returns true if the module is enabled,
     * but it can be overridden to provide custom behavior (e.g. a module that listens for events even when disabled).
     *
     * @return Whether the module should listen for events.
     */
    @Override
    public boolean listening() {
        return this.isEnabled();
    }

    /**
     * Gets the child settings of this module.
     *
     * @return A list of child settings.
     */
    @Override
    public List<AbstractSetting<?>> getChildren() {
        return this.settings.view();
    }

    /**
     * Adds a child setting to this module.
     *
     * @param child The child setting to add.
     * @param <S>   The type of the child setting's value.
     * @return The child setting that was added.
     */
    @Override
    public <S extends AbstractSetting<?>> S setting(S child) {
        return settings.add(child);
    }

    // @qe7
    // TODO: Implement serialization for modules (e.g. enabled state, settings values)

    /**
     * Handles command input for this module.
     * This method will be called when the user inputs a command that starts with the module's name (e.g. .module).
     * Override this method to add custom command handling for the module (e.g. .module setting value).
     *
     * @param args The arguments passed to the command, excluding the module name (e.g. if the user inputs ".module setting value", args will be ["setting", "value"]).
     */
    @Override
    public void run(String[] args) {
        // @qe7
        // TODO: Implement command handling for modules (e.g. .module setting value)
    }

    /**
     * Provides command completions for this module.
     * This method will be called when the user is typing a command that starts with the module's name (e.g. .module) and presses the tab key.
     * Override this method to add custom command completions for the module (e.g. .module setting <setting>).
     *
     * @param args  The arguments passed to the command, excluding the module name
     *              (e.g. if the user is typing ".module setting value", args will be ["setting", "value"]).
     * @param index The index of the argument that the user is currently typing
     *              (e.g. if the user is typing ".module setting value" and the cursor is after "setting",
     *              index will be 0; if the cursor is after "value", index will be 1).
     * @return A list of command completions for the current argument, or an empty list if there are no completions.
     */
    @Override
    public List<String> completions(String[] args, int index) {
        // @qe7
        // TODO: Implement command completions for modules (e.g. .module setting <setting> <value>)
        return Collections.emptyList();
    }
}
