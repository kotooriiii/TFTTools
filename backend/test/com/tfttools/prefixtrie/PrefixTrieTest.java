package com.tfttools.prefixtrie;

import com.tfttools.domain.Champion;
import org.junit.jupiter.api.Test;

import java.util.*;


public class PrefixTrieTest {

    private PrefixNode<Champion> createTestTrieSingle() {
        PrefixNode<Champion> trie = new PrefixNode<>();

        PrefixNode<Champion> curr = trie;
        curr.setChild(0, new PrefixNode<>('A'));
        curr = curr.getChild('A');
        curr.setHasChildren(true);
        curr.setChild(13, new PrefixNode<>('N'));
        curr = curr.getChild('N');
        curr.setHasChildren(true);
        curr.setChild(13, new PrefixNode<>('N'));
        curr = curr.getChild('N');
        curr.setHasChildren(true);
        curr.setChild(8, new PrefixNode<>('I'));
        curr = curr.getChild('I');
        curr.setHasChildren(true);
        curr.setChild(4, new PrefixNode<>('E'));
        curr = curr.getChild('E');
        curr.setData(Champion.ANNIE);

        return trie;
    }

    private PrefixNode<Champion> createTestTrie() {
        PrefixNode<Champion> trie = new PrefixNode<>();

        PrefixNode<Champion> curr = trie;
        curr.setChild(0, new PrefixNode<>('A'));
        curr = curr.getChild('A');
        curr.setHasChildren(true);
        curr.setChild(13, new PrefixNode<>('N'));
        curr = curr.getChild('N');
        curr.setHasChildren(true);
        curr.setChild(13, new PrefixNode<>('N'));
        curr = curr.getChild('N');
        curr.setHasChildren(true);
        curr.setChild(8, new PrefixNode<>('I'));
        curr = curr.getChild('I');
        curr.setHasChildren(true);
        curr.setChild(4, new PrefixNode<>('E'));
        curr = curr.getChild('E');
        curr.setData(Champion.ANNIE);

        curr = trie;
        curr = curr.getChild('A');
        curr.setHasChildren(true);
        curr.setChild(20, new PrefixNode<>('U'));
        curr = curr.getChild('U');
        curr.setHasChildren(true);
        curr.setChild(17, new PrefixNode<>('R'));
        curr = curr.getChild('R');
        curr.setHasChildren(true);
        curr.setChild(14, new PrefixNode<>('O'));
        curr = curr.getChild('O');
        curr.setHasChildren(true);
        curr.setChild(17, new PrefixNode<>('R'));
        curr = curr.getChild('R');
        curr.setHasChildren(true);
        curr.setChild(0, new PrefixNode<>('A'));
        curr = curr.getChild('A');
        curr.setData(Champion.AURORA);

        return trie;
    }

    private PrefixNode<Champion> createTestTriePunctuation() {
        PrefixNode<Champion> trie = new PrefixNode<>();

        PrefixNode<Champion> curr = trie;
        curr.setChild(2, new PrefixNode<>('C'));
        curr = curr.getChild('C');
        curr.setHasChildren(true);
        curr.setChild(7, new PrefixNode<>('H'));
        curr = curr.getChild('H');
        curr.setHasChildren(true);
        curr.setChild(14, new PrefixNode<>('O'));
        curr = curr.getChild('O');
        curr.setHasChildren(true);
        curr.setChild(6, new PrefixNode<>('G'));
        curr = curr.getChild('G');
        curr.setHasChildren(true);
        curr.setChild(0, new PrefixNode<>('A'));
        curr = curr.getChild('A');
        curr.setHasChildren(true);
        curr.setChild(19, new PrefixNode<>('T'));
        curr = curr.getChild('T');
        curr.setHasChildren(true);
        curr.setChild(7, new PrefixNode<>('H'));
        curr = curr.getChild('H');
        curr.setData(Champion.CHO_GATH);

        curr = trie;
        curr.setChild(3, new PrefixNode<>('D'));
        curr = curr.getChild('D');
        curr.setHasChildren(true);
        curr.setChild(17, new PrefixNode<>('R'));
        curr = curr.getChild('R');
        curr.setHasChildren(true);
        curr.setChild(12, new PrefixNode<>('M'));
        curr = curr.getChild('M');
        curr.setHasChildren(true);
        curr.setChild(20, new PrefixNode<>('U'));
        curr = curr.getChild('U');
        curr.setHasChildren(true);
        curr.setChild(13, new PrefixNode<>('N'));
        curr = curr.getChild('N');
        curr.setHasChildren(true);
        curr.setChild(3, new PrefixNode<>('D'));
        curr = curr.getChild('D');
        curr.setHasChildren(true);
        curr.setChild(14, new PrefixNode<>('O'));
        curr = curr.getChild('O');
        curr.setData(Champion.DR_MUNDO);

        return trie;
    }

    /**
     * Tests PrefixTrie.add(),
     */
    @Test
    public void testAddSingleChampion() {
        PrefixNode<Champion> testTrieSingle = createTestTrieSingle();
        PrefixTrie<Champion> test = new PrefixTrie<>();

        test.add(Champion.ANNIE);

        PrefixNode<Champion> curr = testTrieSingle;
        assert test.search("A").equals(curr.getChild('A'));
        curr = curr.getChild('A');
        assert test.search("AN").equals(curr.getChild('N'));
        curr = curr.getChild('N');
        assert test.search("ANN").equals(curr.getChild('N'));
        curr = curr.getChild('N');
        assert test.search("ANNI").equals(curr.getChild('I'));
        curr = curr.getChild('I');
        assert test.search("ANNIE").equals(curr.getChild('E'));
    }

    @Test
    public void testAddMultipleChampions() {
        PrefixNode<Champion> testTrie = createTestTrie();
        PrefixNode<Champion> curr = testTrie;

        PrefixTrie<Champion> test = new PrefixTrie<>();

        test.add(Champion.ANNIE);
        test.add(Champion.AURORA);

        assert test.search("A").equals(curr.getChild('A'));
        curr = curr.getChild('A');
        assert test.search("AN").equals(curr.getChild('N'));
        curr = curr.getChild('N');
        assert test.search("ANN").equals(curr.getChild('N'));
        curr = curr.getChild('N');
        assert test.search("ANNI").equals(curr.getChild('I'));
        curr = curr.getChild('I');
        assert test.search("ANNIE").equals(curr.getChild('E'));

        curr = testTrie;
        assert test.search("A").equals(curr.getChild('A'));
        curr = curr.getChild('A');
        assert test.search("AU").equals(curr.getChild('U'));
        curr = curr.getChild('U');
        assert test.search("AUR").equals(curr.getChild('R'));
        curr = curr.getChild('R');
        assert test.search("AURO").equals(curr.getChild('O'));
        curr = curr.getChild('O');
        assert test.search("AUROR").equals(curr.getChild('R'));
        curr = curr.getChild('R');
        assert test.search("AURORA").equals(curr.getChild('A'));
    }

    @Test
    public void testAddChampionsWithPunctuation() {
        PrefixTrie<Champion> test = new PrefixTrie<>();
        PrefixNode<Champion> testTriePunctuation = createTestTriePunctuation();
        PrefixNode<Champion> curr = testTriePunctuation;

        test.add(Champion.CHO_GATH);
        test.add(Champion.DR_MUNDO);

        assert test.search("C").equals(curr.getChild('C'));
        curr = curr.getChild('C');
        assert test.search("CH").equals(curr.getChild('H'));
        curr = curr.getChild('H');
        assert test.search("CHO").equals(curr.getChild('O'));
        curr = curr.getChild('O');
        assert test.search("CHOG").equals(curr.getChild('G'));
        curr = curr.getChild('G');
        assert test.search("CHOGA").equals(curr.getChild('A'));
        curr = curr.getChild('A');
        assert test.search("CHOGAT").equals(curr.getChild('T'));
        curr = curr.getChild('T');
        assert test.search("CHOGATH").equals(curr.getChild('H'));

        curr = testTriePunctuation;
        assert test.search("D").equals(curr.getChild('D'));
        curr = curr.getChild('D');
        assert test.search("DR").equals(curr.getChild('R'));
        curr = curr.getChild('R');
        assert test.search("DRM").equals(curr.getChild('M'));
        curr = curr.getChild('M');
        assert test.search("DRMU").equals(curr.getChild('U'));
        curr = curr.getChild('U');
        assert test.search("DRMUN").equals(curr.getChild('N'));
        curr = curr.getChild('N');
        assert test.search("DRMUND").equals(curr.getChild('D'));
        curr = curr.getChild('D');
        assert test.search("DRMUNDO").equals(curr.getChild('O'));
    }

    @Test
    public void testGetAllDescendantsByPrefixEmptyString() {
        PrefixTrie<Champion> test = new PrefixTrie<>();

        // add all champions to test trie
        Arrays.stream(Champion.values()).forEach(test::add);

        // collect all champs to truth set
        Set<Champion> allChamps = new HashSet<>(Arrays.asList(Champion.values()));

        assert new HashSet<>(test.getAllDescendantsByPrefix("")).equals(allChamps);
    }


    @Test
    public void testGetAllDescendantsByPrefix() {
        PrefixTrie<Champion> test = new PrefixTrie<>();
        Set<Champion> annieAurora = new HashSet<>(Arrays.asList(Champion.ANNIE, Champion.AURORA));
        Set<Champion> annieAuroraAlistar = new HashSet<>(Arrays.asList(Champion.ANNIE, Champion.AURORA, Champion.ALISTAR));
        Set<Champion> garenGalio = new HashSet<>(Arrays.asList(Champion.GAREN, Champion.GALIO));


        test.add(Champion.ANNIE);
        test.add(Champion.AURORA);

        assert new HashSet<>(test.getAllDescendantsByPrefix("A")).equals(annieAurora);

        test.add(Champion.ALISTAR);

        assert new HashSet<>(test.getAllDescendantsByPrefix("A")).equals(annieAuroraAlistar);

        test.add(Champion.GAREN);
        test.add(Champion.GALIO);

        assert new HashSet<>(test.getAllDescendantsByPrefix("GA")).equals(garenGalio);

    }

    @Test
    public void testSearchNull() {
        PrefixTrie<Champion> test = new PrefixTrie<>();

        test.add(Champion.ANNIE);

        assert test.search("B") == null;
    }

    @Test
    public void testSearchValid() {
        PrefixTrie<Champion> test = new PrefixTrie<>();
        PrefixNode<Champion> testTrieSingle = createTestTrieSingle();
        PrefixNode<Champion> curr = testTrieSingle;

        test.add(Champion.ANNIE);

        assert test.search("A").equals(curr.getChild('A'));
        curr = curr.getChild('A');
        curr = curr.getChild('N');
        assert test.search("ANN").equals(curr.getChild('N'));
    }
}
