package com.tfttools.prefixtrie;

import com.tfttools.domain.Namable;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Implementation of a Prefix tree that accepts generic types.
 *
 * @param <T> Objects of type T that will be stored within the tree
 */
@Component
public class PrefixTrie<T extends Namable> {

    PrefixNode<T> root;

    public PrefixTrie() {
        this.root = new PrefixNode<>();
    }

    /**
     * Adds an object to the tree
     *
     * @param obj The object to be added to the tree
     */
    public void add(T obj) {
        PrefixNode<T> curr = this.root;

        // perform some input validation, remove some punctuation and whitespace
        String s = PrefixTrieUtils.removePunctuation(obj.toString());

        for (int i = 0; i < s.length(); i++) {
            if (curr.childExists(s.charAt(i))) {
                curr = curr.getChild(s.charAt(i));
            } else {
                curr.children.set(curr.getChildIndex(s.charAt(i)), new PrefixNode<>(s.charAt(i)));
                curr.setHasChildren(true);
                curr = curr.getChild(s.charAt(i));
            }
        }
        curr.setData(obj);
    }

    /**
     * Gets all the descendants of a given prefix
     *
     * @param prefix The prefix to search for
     * @return List of type T of all the completed descendants of the prefix
     */
    public List<T> getAllDescendantsByPrefix(String prefix) {
        PrefixNode<T> n = search(prefix);

        if (n != null) {
            return getAllDescendants(n);
        } else {
            return Collections.emptyList();
        }

    }

    private PrefixNode<T> search(String prefix) {
        PrefixNode<T> curr = this.root;

        for (int i = 0; i < prefix.length(); i++) {
            if (curr.childExists(prefix.charAt(i))) {
                curr = curr.getChild(prefix.charAt(i));
            } else {
                return null;
            }
        }

        return curr;
    }

    private List<T> getAllDescendants(PrefixNode<T> node) {
        List<T> descendants = new ArrayList<>();
        _getAllDescendants(node, descendants);

        return descendants;
    }

    private void _getAllDescendants(PrefixNode<T> node, List<T> descendants) {
        if (node.data != null) {
            descendants.add(node.data);
        }

        // base case we find a leaf node
        if (!node.hasChildren) {
            return;
        }

        node.children.stream()
                .filter(Objects::nonNull)
                .forEach(prefixNode -> _getAllDescendants(prefixNode, descendants));

    }
}



