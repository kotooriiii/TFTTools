package com.tfttools.prefixtrie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a node in a Prefix tree
 *
 * @param <T> Object of type T to be stored in each node
 */
public class PrefixNode<T>
{
    Character c;
    T data;
    List<PrefixNode<T>> children;
    boolean hasChildren;
    private static final int ASCII_RANGE_LOW = 65;
    private static final int ASCII_RANGE_HIGH = 90;
    private static final int LEN_ALPHABET = ASCII_RANGE_HIGH - ASCII_RANGE_LOW + 2; // HIGH + LOW + 1 = alphanet. + 1 more is '&'.

    /**
     * Default constructor for an empty node
     */
    public PrefixNode()
    {
        this.c = null;
        this.data = null;
        this.children = new ArrayList<>(Collections.nCopies(LEN_ALPHABET, null));
        this.hasChildren = false;
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
        this.children = new ArrayList<>(Collections.nCopies(LEN_ALPHABET, null));
        this.hasChildren = false;
    }

    /**
     * Checks if a given character exists in the children of this node
     *
     * @param c The character to be checked
     * @return If character exists in children list
     */
    public boolean childExists(char c)
    {
        return getChild(c) != null;
    }

    /**
     * Gets the Prefix node of given character c
     *
     * @param c The character to be searched for
     * @return Prefix node of char c
     */
    public PrefixNode<T> getChild(char c)
    {
        return this.children.get(getChildIndex(c));
    }

    /**
     * Gets the index of the underlying children list for a character c
     *
     * @param c The character to be searched for
     * @return Index of the character c
     */
    public int getChildIndex(char c)
    {
        if (((int) c >= ASCII_RANGE_LOW) && ((int) c <= ASCII_RANGE_HIGH))
        {
            return (int) c - ASCII_RANGE_LOW;
        }
        if (c == '&')
        {
            return ASCII_RANGE_HIGH - ASCII_RANGE_LOW + 1;
        }
        return -1;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setChild(int index, PrefixNode<T> child) {
        this.children.set(index, child); }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PrefixNode<?> that = (PrefixNode<?>) o;
        return hasChildren == that.hasChildren && Objects.equals(c, that.c) && Objects.equals(data, that.data) && Objects.equals(children, that.children);
    }
}
