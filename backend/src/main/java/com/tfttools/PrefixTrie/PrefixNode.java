package com.tfttools.PrefixTrie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a node in a Prefix tree
 * @param <T> Object of type T to be stored in each node
 */
public class PrefixNode<T> {
    Character c;
    T data;
    List<PrefixNode<T>> children;
    boolean hasChildren;
    private static final int LEN_ALPHABET = 26;
    private static final int ASCII_RANGE_LOW = 65;
    private static final int ASCII_RANGE_HIGH = 90;

    /**
     * Default constructor for an empty node
     */
    public PrefixNode() {
        this.c = null;
        this.data = null;
        this.children = new ArrayList<>(Collections.nCopies(LEN_ALPHABET, null));
        this.hasChildren = false;
    }

    /**
     * Constructor for a node with some data
     * @param c The character to be stored in the node
     */
    public PrefixNode(char c) {
        this.c = c;
        this.data = null;
        this.children = new ArrayList<>(Collections.nCopies(LEN_ALPHABET, null));
        this.hasChildren = false;
    }

    /**
     * Checks if a given character exists in the children of this node
     * @param c The character to be checked
     * @return If character exists in children list
     */
    public boolean childExists(char c) {
        if (((int) c >= ASCII_RANGE_LOW) && ((int) c <= ASCII_RANGE_HIGH)){
            return this.children.get((int) c - ASCII_RANGE_LOW) != null;
        }

        return false;
    }

    /**
     * Gets the Prefix node of given character c
     * @param c The character to be searched for
     * @return Prefix node of char c
     */
    public PrefixNode<T> getChild(char c) {
        if (((int) c >= ASCII_RANGE_LOW) && ((int) c <= ASCII_RANGE_HIGH)){
            return this.children.get((int) c - ASCII_RANGE_LOW);
        }

        return null;
    }

    /**
     * Gets the index of the underlying children list for a character c
     * @param c The character to be searched for
     * @return Index of the character c
     */
    public int getChildIndex(char c) {
        if (((int) c >= ASCII_RANGE_LOW) && ((int) c <= ASCII_RANGE_HIGH)){
            return (int) c - ASCII_RANGE_LOW;
        }

        return -1;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
    public void setData(T data) { this.data = data; }
}
