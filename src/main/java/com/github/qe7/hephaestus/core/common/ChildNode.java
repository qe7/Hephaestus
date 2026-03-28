package com.github.qe7.hephaestus.core.common;

import java.util.Optional;

/**
 * Represents a node that has a parent node.
 * The parent node can be used to access the child node's siblings and other related nodes.
 * The child node can also be used to access the parent node's properties and methods.
 * This interface is useful for creating a hierarchical structure of nodes, where each node can have a parent and multiple children.
 *
 * @param <P> the type of the parent node
 */
public interface ChildNode<P> {
    Optional<P> getParent();

    void attachTo(P parent);
}
