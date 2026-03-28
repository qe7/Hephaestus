package com.github.qe7.hephaestus.core.common;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A registry for child nodes that are attached to a parent node.
 * This class is used to manage the child nodes of a parent node.
 * The parent node is passed in the constructor and the child nodes are added using the add method.
 * The view method returns an unmodifiable list of the child nodes.
 * The child nodes must implement the ChildNode interface,
 * which requires them to have a reference to their parent node and a method to attach to the parent node.
 * The ChildRegistry class is designed to be used in a fluent API style,
 * allowing for method chaining when adding child nodes.
 *
 * @param <P> the type of the parent node
 * @param <C> the type of the child nodes, which must extend ChildNode<P>
 */
@RequiredArgsConstructor
public final class ChildRegistry<P, C extends ChildNode<P>> {
    private final P parent;
    private final List<C> children = new ArrayList<>();

    public <T extends C> T add(T child) {
        Objects.requireNonNull(child, "child");
        child.attachTo(parent);
        children.add(child);
        return child;
    }

    public List<C> view() {
        return Collections.unmodifiableList(children);
    }
}
