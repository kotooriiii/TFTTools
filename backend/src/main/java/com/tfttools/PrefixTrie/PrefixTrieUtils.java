package com.tfttools.PrefixTrie;

public class PrefixTrieUtils {
    public static String removePunctuation(String text) {
        return text.toUpperCase().replaceAll("[,.'_\\-\\s]", "");
    }
}
