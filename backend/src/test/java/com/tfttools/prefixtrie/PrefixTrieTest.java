package com.tfttools.prefixtrie;

import com.tfttools.domain.Nameable;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PrefixTrieTest {

    private record TestEntry(String name) implements Nameable {
        @Override
        public String getDisplayName() {
            return name;
        }
    }

    private static final TestEntry ANNIE = new TestEntry("Annie");
    private static final TestEntry AURORA = new TestEntry("Aurora");
    private static final TestEntry ALISTAR = new TestEntry("Alistar");
    private static final TestEntry GAREN = new TestEntry("Garen");
    private static final TestEntry GALIO = new TestEntry("Galio");
    private static final TestEntry CHOGATH = new TestEntry("Cho'Gath");
    private static final TestEntry DR_MUNDO = new TestEntry("Dr. Mundo");
    private static final TestEntry PARENTHESIZED = new TestEntry("Test (v2)");

    @Test
    public void testAddAndSearchSingleEntry() {
        PrefixTrie<TestEntry> trie = new PrefixTrie<>();
        trie.add(ANNIE);

        assertNotNull(trie.search("A"));
        assertNotNull(trie.search("AN"));
        assertNotNull(trie.search("ANN"));
        assertNotNull(trie.search("ANNI"));
        assertEquals(List.of(ANNIE), trie.getAllDescendantsByPrefix("ANNIE"));
    }

    @Test
    public void testAddAndSearchMultipleEntriesSharingPrefix() {
        PrefixTrie<TestEntry> trie = new PrefixTrie<>();
        trie.add(ANNIE);
        trie.add(AURORA);

        assertEquals(new HashSet<>(List.of(ANNIE, AURORA)), new HashSet<>(trie.getAllDescendantsByPrefix("A")));
        assertEquals(List.of(ANNIE), trie.getAllDescendantsByPrefix("ANN"));
        assertEquals(List.of(AURORA), trie.getAllDescendantsByPrefix("AU"));
    }

    @Test
    public void testExistingPunctuationIsStripped() {
        PrefixTrie<TestEntry> trie = new PrefixTrie<>();
        trie.add(CHOGATH);
        trie.add(DR_MUNDO);

        // apostrophe stripped
        assertEquals(List.of(CHOGATH), trie.getAllDescendantsByPrefix("ChoGath"));
        // period and whitespace stripped
        assertEquals(List.of(DR_MUNDO), trie.getAllDescendantsByPrefix("DrMun"));
    }

    /**
     * Regression test for TFTTOOLS-18: names containing characters outside A-Z/'&'
     * (e.g. parentheses, digits) must not throw when added or searched.
     */
    @Test
    public void testParenthesesAndDigitsAreSupported() {
        PrefixTrie<TestEntry> trie = new PrefixTrie<>();

        assertDoesNotThrow(() -> trie.add(PARENTHESIZED));

        assertEquals(List.of(PARENTHESIZED), trie.getAllDescendantsByPrefix("TEST(V2)"));
        assertEquals(List.of(PARENTHESIZED), trie.getAllDescendantsByPrefix("TEST(V"));
        assertNotNull(trie.search("TEST(V2)"));
    }

    @Test
    public void testSearchReturnsNullForUnknownPrefix() {
        PrefixTrie<TestEntry> trie = new PrefixTrie<>();
        trie.add(ANNIE);

        assertNull(trie.search("B"));
        assertNull(trie.search("ANNIEX"));
    }

    @Test
    public void testGetAllDescendantsByPrefixEmptyStringReturnsAll() {
        PrefixTrie<TestEntry> trie = new PrefixTrie<>();
        Set<TestEntry> all = Set.of(ANNIE, AURORA, ALISTAR, GAREN, GALIO);
        all.forEach(trie::add);

        assertEquals(all, new HashSet<>(trie.getAllDescendantsByPrefix("")));
    }

    @Test
    public void testGetAllDescendantsByPrefixNarrowsResults() {
        PrefixTrie<TestEntry> trie = new PrefixTrie<>();
        trie.add(ANNIE);
        trie.add(AURORA);
        trie.add(ALISTAR);
        trie.add(GAREN);
        trie.add(GALIO);

        assertEquals(new HashSet<>(List.of(ANNIE, AURORA, ALISTAR)), new HashSet<>(trie.getAllDescendantsByPrefix("A")));
        assertEquals(new HashSet<>(List.of(GAREN, GALIO)), new HashSet<>(trie.getAllDescendantsByPrefix("GA")));
    }
}
