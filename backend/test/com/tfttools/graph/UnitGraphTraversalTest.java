package com.tfttools.graph;

import com.tfttools.domain.Champion;
import com.tfttools.domain.Unit;
import com.tfttools.registry.UnitRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.tfttools.domain.Trait;

import java.util.*;

public class UnitGraphTraversalTest {
    UnitRegistry unitRegistry;

    @BeforeEach
    public void prepareTests() {
        this.unitRegistry = new UnitRegistry();
    }

    @Test
    public void testConstructorEmpty() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of()),
                new ArrayList<Integer>(List.of()),
                new ArrayList<Champion>(List.of()),
                unitRegistry
        );

        Map<Trait, Float> currentTraits = traversal.getCurrentTraits();
        List<Unit> requiredUnits = traversal.getRequiredUnits();
        List<Trait> requiredTraits = traversal.getRequiredTraits();
        Map<Trait, Integer> thresholds = traversal.getThresholds();
        Set<Unit> visited = traversal.getVisited();

        assertEquals(currentTraits, Map.of(
                Trait.DIVINICORP, 0f,
                Trait.OVERLORD, 0f,
                Trait.GOD_OF_THE_NET, 0f,
                Trait.SOUL_KILLER, 0f,
                Trait.VIRUS, 0f
        ));

        assertEquals(requiredTraits, Collections.emptyList());
        assertEquals(thresholds, Collections.emptyMap());
        assertEquals(requiredUnits, Collections.emptyList());
        assertEquals(visited, Collections.emptySet());
    }

    @Test
    public void testConstructorSingleRequiredChampion() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of()),
                new ArrayList<Integer>(List.of()),
                new ArrayList<Champion>(List.of(Champion.KINDRED)),
                unitRegistry
        );

        Map<Trait, Float> currentTraits = traversal.getCurrentTraits();
        List<Unit> requiredUnits = traversal.getRequiredUnits();
        List<Trait> requiredTraits = traversal.getRequiredTraits();
        Map<Trait, Integer> thresholds = traversal.getThresholds();
        Set<Unit> visited = traversal.getVisited();

        assertEquals(currentTraits, Map.of(
                Trait.DIVINICORP, 0f,
                Trait.OVERLORD, 0f,
                Trait.GOD_OF_THE_NET, 0f,
                Trait.SOUL_KILLER, 0f,
                Trait.VIRUS, 0f,
                Trait.NITRO, 1f,
                Trait.MARKSMAN, 1f,
                Trait.RAPIDFIRE, 1f
        ));

        assertEquals(requiredTraits, Collections.emptyList());
        assertEquals(thresholds, Collections.emptyMap());

        assertEquals(requiredUnits, List.of(
                unitRegistry.getUnitByChampion(Champion.KINDRED)
        ));

        assertEquals(visited, Set.of(
                unitRegistry.getUnitByChampion(Champion.KINDRED)
        ));
    }

    @Test
    public void testConstructorMultipleRequiredChampions() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of()),
                new ArrayList<Integer>(List.of()),
                new ArrayList<Champion>(List.of(Champion.KINDRED, Champion.NIDALEE)),
                unitRegistry
        );

        Map<Trait, Float> currentTraits = traversal.getCurrentTraits();
        List<Unit> requiredUnits = traversal.getRequiredUnits();
        List<Trait> requiredTraits = traversal.getRequiredTraits();
        Map<Trait, Integer> thresholds = traversal.getThresholds();
        Set<Unit> visited = traversal.getVisited();

        assertEquals(currentTraits, Map.of(
                Trait.DIVINICORP, 0f,
                Trait.OVERLORD, 0f,
                Trait.GOD_OF_THE_NET, 0f,
                Trait.SOUL_KILLER, 0f,
                Trait.VIRUS, 0f,
                Trait.NITRO, 2f,
                Trait.MARKSMAN, 1f,
                Trait.RAPIDFIRE, 1f,
                Trait.AMP, 1f
        ));

        assertEquals(requiredTraits, Collections.emptyList());
        assertEquals(thresholds, Collections.emptyMap());

        assertEquals(requiredUnits, List.of(
                unitRegistry.getUnitByChampion(Champion.KINDRED),
                unitRegistry.getUnitByChampion(Champion.NIDALEE)
        ));

        assertEquals(visited, Set.of(
                unitRegistry.getUnitByChampion(Champion.KINDRED),
                unitRegistry.getUnitByChampion(Champion.NIDALEE)
        ));
    }

    @Test
    public void testConstructorSingleRequiredTrait() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of(Trait.STRATEGIST)),
                new ArrayList<Integer>(List.of(2)),
                new ArrayList<Champion>(List.of()),
                unitRegistry
        );

        Map<Trait, Float> currentTraits = traversal.getCurrentTraits();
        List<Unit> requiredUnits = traversal.getRequiredUnits();
        List<Trait> requiredTraits = traversal.getRequiredTraits();
        Map<Trait, Integer> thresholds = traversal.getThresholds();
        Set<Unit> visited = traversal.getVisited();

        assertEquals(currentTraits, Map.of(
                Trait.DIVINICORP, 0f,
                Trait.OVERLORD, 0f,
                Trait.GOD_OF_THE_NET, 0f,
                Trait.SOUL_KILLER, 0f,
                Trait.VIRUS, 0f,
                Trait.STRATEGIST, 0f
        ));

        assertEquals(requiredTraits, List.of(
                Trait.STRATEGIST
        ));
        assertEquals(thresholds, Map.of(
                Trait.STRATEGIST, 2
        ));

        assertEquals(requiredUnits, Collections.emptyList());
        assertEquals(visited, Collections.emptySet());
    }

    @Test
    public void testConstructorMultipleRequiredTraits() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of(Trait.STRATEGIST, Trait.STREET_DEMON)),
                new ArrayList<Integer>(List.of(2, 3)),
                new ArrayList<Champion>(List.of()),
                unitRegistry
        );

        Map<Trait, Float> currentTraits = traversal.getCurrentTraits();
        List<Unit> requiredUnits = traversal.getRequiredUnits();
        List<Trait> requiredTraits = traversal.getRequiredTraits();
        Map<Trait, Integer> thresholds = traversal.getThresholds();
        Set<Unit> visited = traversal.getVisited();

        assertEquals(currentTraits, Map.of(
                Trait.DIVINICORP, 0f,
                Trait.OVERLORD, 0f,
                Trait.GOD_OF_THE_NET, 0f,
                Trait.SOUL_KILLER, 0f,
                Trait.VIRUS, 0f,
                Trait.STRATEGIST, 0f,
                Trait.STREET_DEMON, 0f
        ));

        assertEquals(requiredTraits, List.of(
                Trait.STRATEGIST,
                Trait.STREET_DEMON
        ));
        assertEquals(thresholds, Map.of(
                Trait.STRATEGIST, 2,
                Trait.STREET_DEMON, 3
        ));

        assertEquals(requiredUnits, Collections.emptyList());
        assertEquals(visited, Collections.emptySet());
    }

    @Test
    public void testConstructorSingleTraitAndChampion() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of(Trait.STRATEGIST)),
                new ArrayList<Integer>(List.of(2)),
                new ArrayList<Champion>(List.of(Champion.NEEKO)),
                unitRegistry
        );

        Map<Trait, Float> currentTraits = traversal.getCurrentTraits();
        List<Unit> requiredUnits = traversal.getRequiredUnits();
        List<Trait> requiredTraits = traversal.getRequiredTraits();
        Map<Trait, Integer> thresholds = traversal.getThresholds();
        Set<Unit> visited = traversal.getVisited();

        assertEquals(currentTraits, Map.of(
                Trait.DIVINICORP, 0f,
                Trait.OVERLORD, 0f,
                Trait.GOD_OF_THE_NET, 0f,
                Trait.SOUL_KILLER, 0f,
                Trait.VIRUS, 0f,
                Trait.STRATEGIST, 1f,
                Trait.STREET_DEMON, 1f
        ));

        assertEquals(requiredTraits, List.of(
                Trait.STRATEGIST
        ));
        assertEquals(thresholds, Map.of(
                Trait.STRATEGIST, 2
        ));

        assertEquals(requiredUnits, List.of(
                unitRegistry.getUnitByChampion(Champion.NEEKO)
        ));
        assertEquals(visited, Set.of(
                unitRegistry.getUnitByChampion(Champion.NEEKO)
        ));
    }

    @Test
    public void testConstructorMultiTraitsAndChampions() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of(Trait.STRATEGIST, Trait.STREET_DEMON)),
                new ArrayList<Integer>(List.of(2, 3)),
                new ArrayList<Champion>(List.of(Champion.NEEKO, Champion.EKKO)),
                unitRegistry
        );

        Map<Trait, Float> currentTraits = traversal.getCurrentTraits();
        List<Unit> requiredUnits = traversal.getRequiredUnits();
        List<Trait> requiredTraits = traversal.getRequiredTraits();
        Map<Trait, Integer> thresholds = traversal.getThresholds();
        Set<Unit> visited = traversal.getVisited();

        assertEquals(currentTraits, Map.of(
                Trait.DIVINICORP, 0f,
                Trait.OVERLORD, 0f,
                Trait.GOD_OF_THE_NET, 0f,
                Trait.SOUL_KILLER, 0f,
                Trait.VIRUS, 0f,
                Trait.STRATEGIST, 2f,
                Trait.STREET_DEMON, 2f
        ));

        assertEquals(requiredTraits, List.of(
                Trait.STRATEGIST,
                Trait.STREET_DEMON
        ));
        assertEquals(thresholds, Map.of(
                Trait.STRATEGIST, 2,
                Trait.STREET_DEMON, 3
        ));

        assertEquals(requiredUnits, List.of(
                unitRegistry.getUnitByChampion(Champion.NEEKO),
                unitRegistry.getUnitByChampion(Champion.EKKO)
        ));
        assertEquals(visited, Set.of(
                unitRegistry.getUnitByChampion(Champion.NEEKO),
                unitRegistry.getUnitByChampion(Champion.EKKO)
        ));
    }

    @Test
    public void testCreateCompNoParams() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of()),
                new ArrayList<Integer>(List.of()),
                new ArrayList<Champion>(List.of()),
                unitRegistry
        );

        assertEquals(1, traversal.createComp(1).size());
        assertEquals(3, traversal.createComp(3).size());
        assertEquals(5, traversal.createComp(5).size());
        assertEquals(8, traversal.createComp(8).size());
    }

    @Test
    public void testCreateCompSingleRequiredChampion() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of()),
                new ArrayList<Integer>(List.of()),
                new ArrayList<Champion>(List.of(Champion.KINDRED)),
                unitRegistry
        );

        List<Unit> singleUnit = traversal.createComp(1);
        assertEquals(1, singleUnit.size());
        assertTrue(singleUnit.contains(unitRegistry.getUnitByChampion(Champion.KINDRED)));

        List<Unit> multiUnit = traversal.createComp(8);
        assertEquals(8, multiUnit.size());
        assertTrue(multiUnit.contains(unitRegistry.getUnitByChampion(Champion.KINDRED)));
    }

    @Test
    public void testCreateCompMultiRequiredChampions() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of()),
                new ArrayList<Integer>(List.of()),
                new ArrayList<Champion>(List.of(Champion.KINDRED, Champion.JHIN)),
                unitRegistry
        );

        List<Unit> minUnits = traversal.createComp(2);
        assertEquals(2, minUnits.size());
        assertTrue(minUnits.contains(unitRegistry.getUnitByChampion(Champion.KINDRED)));
        assertTrue(minUnits.contains(unitRegistry.getUnitByChampion(Champion.JHIN)));

        List<Unit> multiUnit = traversal.createComp(8);
        assertEquals(8, multiUnit.size());
        assertTrue(multiUnit.contains(unitRegistry.getUnitByChampion(Champion.KINDRED)));
        assertTrue(multiUnit.contains(unitRegistry.getUnitByChampion(Champion.JHIN)));

    }

    @Test
    public void testCreateCompSingleRequiredTrait() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of(Trait.STRATEGIST)),
                new ArrayList<Integer>(List.of(2)),
                new ArrayList<Champion>(List.of()),
                unitRegistry
        );

        List<Unit> minUnits = traversal.createComp(2);
        assertEquals(2, minUnits.size());
        assertTrue(countTraits(minUnits).containsKey(Trait.STRATEGIST));
        assertEquals(2, countTraits(minUnits).get(Trait.STRATEGIST));

        List<Unit> multiUnits = traversal.createComp(8);
        assertEquals(8, multiUnits.size());
        assertTrue(countTraits(multiUnits).containsKey(Trait.STRATEGIST));
        assertTrue(2 <= countTraits(multiUnits).get(Trait.STRATEGIST));
    }

    @Test
    public void testCreateCompMultiRequiredTrait() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of(Trait.STRATEGIST, Trait.STREET_DEMON)),
                new ArrayList<Integer>(List.of(2, 3)),
                new ArrayList<Champion>(List.of()),
                unitRegistry
        );

        List<Unit> minUnits = traversal.createComp(3);
        assertEquals(3, minUnits.size());
        assertTrue(countTraits(minUnits).containsKey(Trait.STRATEGIST));
        assertTrue(2 <= countTraits(minUnits).get(Trait.STRATEGIST));
        assertTrue(countTraits(minUnits).containsKey(Trait.STREET_DEMON));
        assertTrue(3 <= countTraits(minUnits).get(Trait.STREET_DEMON));

        List<Unit> multiUnits = traversal.createComp(8);
        assertEquals(8, multiUnits.size());
        assertTrue(countTraits(multiUnits).containsKey(Trait.STRATEGIST));
        assertTrue(2 <= countTraits(multiUnits).get(Trait.STRATEGIST));
        assertTrue(countTraits(multiUnits).containsKey(Trait.STREET_DEMON));
        assertTrue(3 <= countTraits(multiUnits).get(Trait.STREET_DEMON));
    }

    @Test
    public void testCreateCompSingleChampionAndTrait() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of(Trait.STRATEGIST)),
                new ArrayList<Integer>(List.of(2)),
                new ArrayList<Champion>(List.of(Champion.NEEKO)),
                unitRegistry
        );

        List<Unit> minUnits = traversal.createComp(2);
        assertEquals(2, minUnits.size());
        assertTrue(minUnits.contains(unitRegistry.getUnitByChampion(Champion.NEEKO)));
        assertTrue(countTraits(minUnits).containsKey(Trait.STRATEGIST));
        assertTrue(2 <= countTraits(minUnits).get(Trait.STRATEGIST));

        List<Unit> multiUnits = traversal.createComp(8);
        assertEquals(8, multiUnits.size());
        assertTrue(multiUnits.contains(unitRegistry.getUnitByChampion(Champion.NEEKO)));
        assertTrue(countTraits(multiUnits).containsKey(Trait.STRATEGIST));
        assertTrue(2 <= countTraits(multiUnits).get(Trait.STRATEGIST));
    }

    @Test
    public void testCreateCompMultiChampionsAndTraits() {
        UnitGraphTraversal traversal = new UnitGraphTraversal(
                new ArrayList<Trait>(List.of(Trait.STRATEGIST, Trait.STREET_DEMON)),
                new ArrayList<Integer>(List.of(2, 3)),
                new ArrayList<Champion>(List.of(Champion.NEEKO, Champion.EKKO, Champion.ZIGGS)),
                unitRegistry
        );

        List<Unit> minUnits = traversal.createComp(4);
        assertEquals(4, minUnits.size());
        assertTrue(minUnits.contains(unitRegistry.getUnitByChampion(Champion.NEEKO)));
        assertTrue(minUnits.contains(unitRegistry.getUnitByChampion(Champion.EKKO)));
        assertTrue(minUnits.contains(unitRegistry.getUnitByChampion(Champion.ZIGGS)));
        assertTrue(countTraits(minUnits).containsKey(Trait.STRATEGIST));
        assertTrue(2 <= countTraits(minUnits).get(Trait.STRATEGIST));
        assertTrue(countTraits(minUnits).containsKey(Trait.STREET_DEMON));
        assertTrue(3 <= countTraits(minUnits).get(Trait.STREET_DEMON));

        List<Unit> multiUnits = traversal.createComp(8);
        assertEquals(8, multiUnits.size());
        assertTrue(multiUnits.contains(unitRegistry.getUnitByChampion(Champion.NEEKO)));
        assertTrue(multiUnits.contains(unitRegistry.getUnitByChampion(Champion.EKKO)));
        assertTrue(multiUnits.contains(unitRegistry.getUnitByChampion(Champion.ZIGGS)));
        assertTrue(countTraits(multiUnits).containsKey(Trait.STRATEGIST));
        assertTrue(2 <= countTraits(multiUnits).get(Trait.STRATEGIST));
        assertTrue(countTraits(multiUnits).containsKey(Trait.STREET_DEMON));
        assertTrue(3 <= countTraits(multiUnits).get(Trait.STREET_DEMON));
    }

    private Map<Trait, Integer> countTraits(List<Unit> comp) {
        Map<Trait, Integer> traits = new HashMap<>();

        for (Unit unit : comp) {
            for (Trait trait : unit.getTraits()) {
                if (traits.containsKey(trait)) {
                    traits.put(trait, traits.get(trait) + 1);
                } else {
                    traits.put(trait, 1);
                }
            }
        }

        return traits;
    }

}
