package com.github.qe7.hephaestus.core.feature.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a command that can be executed by the user.
 * Commands can have aliases and usage information.
 */
@RequiredArgsConstructor
@Getter
@Setter
public abstract class AbstractCommand {

    protected final Minecraft minecraft = Minecraft.getMinecraft();

    private final String name;

    private final List<String> aliases = new ArrayList<>();

    private String usage = "No usage provided";

    public abstract void run(final String[] args);

    public abstract List<String> completions(final String[] args, final int index);
}
