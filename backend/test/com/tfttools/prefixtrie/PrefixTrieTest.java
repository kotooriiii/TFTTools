package com.tfttools.prefixtrie;

import com.tfttools.domain.Unit;
import com.tfttools.registry.UnitRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;


public class PrefixTrieTest {
    private UnitRegistry unitRegistry;

    @BeforeEach
    public void initSet() {
        this.unitRegistry = new UnitRegistry("14");
    }

    public PrefixNode<Unit> createTestTrieSingle() {
        PrefixNode<Unit> trie = new PrefixNode<>();

        PrefixNode<Unit> curr = trie;
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
        curr.setData(unitRegistry.getUnitByName("Annie"));

        return trie;
    }

    private PrefixNode<Unit> createTestTrie() {
        PrefixNode<Unit> trie = new PrefixNode<>();

        PrefixNode<Unit> curr = trie;
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
        curr.setData(unitRegistry.getUnitByName("Annie"));

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
        curr.setData(unitRegistry.getUnitByName("Aurora"));

        return trie;
    }

    private PrefixNode<Unit> createTestTriePunctuation() {
        PrefixNode<Unit> trie = new PrefixNode<>();

        PrefixNode<Unit> curr = trie;
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
        curr.setData(unitRegistry.getUnitByName("Cho'Gath"));

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
        curr.setData(unitRegistry.getUnitByName("Dr. Mundo"));

        return trie;
    }

    /**
     * Tests PrefixTrie.add(),
     */
    @Test
    public void testAddSingleUnit() {
        PrefixNode<Unit> testTrieSingle = createTestTrieSingle();
        PrefixTrie<Unit> test = new PrefixTrie<>();

        test.add(unitRegistry.getUnitByName("Annie"));

        PrefixNode<Unit> curr = testTrieSingle;
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
    public void testAddMultipleUnits() {
        PrefixNode<Unit> testTrie = createTestTrie();
        PrefixNode<Unit> curr = testTrie;

        PrefixTrie<Unit> test = new PrefixTrie<>();

        test.add(unitRegistry.getUnitByName("Annie"));
        test.add(unitRegistry.getUnitByName("Aurora"));

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
    public void testAddUnitsWithPunctuation() {
        PrefixTrie<Unit> test = new PrefixTrie<>();
        PrefixNode<Unit> testTriePunctuation = createTestTriePunctuation();
        PrefixNode<Unit> curr = testTriePunctuation;

        test.add(unitRegistry.getUnitByName("Cho'Gath"));
        test.add(unitRegistry.getUnitByName("Dr. Mundo"));

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
        PrefixTrie<Unit> test = new PrefixTrie<>();

        // add all Units to test trie
        unitRegistry.getAllUnits().forEach(test::add);

        // collect all champs to truth set
        Set<Unit> allChamps = new HashSet<>(unitRegistry.getAllUnits());

        assert new HashSet<>(test.getAllDescendantsByPrefix("")).equals(allChamps);
    }


    @Test
    public void testGetAllDescendantsByPrefix() {
        PrefixTrie<Unit> test = new PrefixTrie<>();
        Set<Unit> annieAurora = new HashSet<>(Arrays.asList(unitRegistry.getUnitByName("Annie"), unitRegistry.getUnitByName("Aurora")));
        Set<Unit> annieAuroraAlistar = new HashSet<>(Arrays.asList(unitRegistry.getUnitByName("Annie"), unitRegistry.getUnitByName("Aurora"), unitRegistry.getUnitByName("Alistar")));
        Set<Unit> garenGalio = new HashSet<>(Arrays.asList(unitRegistry.getUnitByName("Garen"), unitRegistry.getUnitByName("Galio")));


        test.add(unitRegistry.getUnitByName("Annie"));
        test.add(unitRegistry.getUnitByName("Aurora"));

        assert new HashSet<>(test.getAllDescendantsByPrefix("A")).equals(annieAurora);

        test.add(unitRegistry.getUnitByName("Alistar"));

        assert new HashSet<>(test.getAllDescendantsByPrefix("A")).equals(annieAuroraAlistar);

        test.add(unitRegistry.getUnitByName("Garen"));
        test.add(unitRegistry.getUnitByName("Galio"));

        assert new HashSet<>(test.getAllDescendantsByPrefix("GA")).equals(garenGalio);

    }

    @Test
    public void testSearchNull() {
        PrefixTrie<Unit> test = new PrefixTrie<>();

        test.add(unitRegistry.getUnitByName("Annie"));

        assert test.search("B") == null;
    }

    @Test
    public void testSearchValid() {
        PrefixTrie<Unit> test = new PrefixTrie<>();
        PrefixNode<Unit> testTrieSingle = createTestTrieSingle();
        PrefixNode<Unit> curr = testTrieSingle;

        test.add(unitRegistry.getUnitByName("Annie"));

        assert test.search("A").equals(curr.getChild('A'));
        curr = curr.getChild('A');
        curr = curr.getChild('N');
        assert test.search("ANN").equals(curr.getChild('N'));
    }
}
