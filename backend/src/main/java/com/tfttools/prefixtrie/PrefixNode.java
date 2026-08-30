package com.tfttools.prefixtrie;

import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a node in a Prefix tree
 *
 * @param <T> Object of type T to be stored in each node
 */
public class PrefixNode<T>
{
    Character c;
    @Setter
    T data;
    Map<Character, PrefixNode<T>> children;

    /**
     * Default constructor for an empty node
     */
    public PrefixNode()
    {
        this.c = null;
        this.data = null;
        this.children = new HashMap<>();
    }

    /**
     * Constructor for a node with some data
     *
     * @param c The character to be stored in the node
     */
    public PrefixNode(char c)
    {
        this.c = c;
        this.data = null;
        this.children = new HashMap<>();
    }

    /**
     * Checks if a given character exists in the children of this node
     *
     * @param c The character to be checked
     * @return If character exists in children list
     */
    public boolean childExists(char c)
    {
        return this.children.containsKey(c);
    }

    /**
     * Gets the Prefix node of given character c
     *
     * @param c The character to be searched for
     * @return Prefix node of char c
     */
    public PrefixNode<T> getChild(char c)
    {
        return this.children.get(c);
    }

    /**
     * Adds a child node for a given character
     *
     * @param c The character the child node represents
     * @param child The child node
     */
    public void addChild(char c, PrefixNode<T> child) {
        this.children.put(c, child);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PrefixNode<?> that = (PrefixNode<?>) o;
        return Objects.equals(c, that.c) && Objects.equals(data, that.data) && Objects.equals(children, that.children);
    }
}
