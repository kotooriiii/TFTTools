package com.tfttools.graph;

import com.tfttools.domain.Champion;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.registry.UnitRegistry;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class to traverse unit / trait graph
 */
public class UnitGraphTraversal {
    private final Map<Trait, Float> currentTraits = new HashMap<>();
    private final UnitRegistry unitRegistry;
    private final Set<Unit> visited = new HashSet<>();
    private final Map<Trait, Integer> thresholds;
    private final List<Unit> requiredUnits;
    private final List<Trait> requiredTraits;

    public UnitGraphTraversal(List<Trait> requiredTraits, List<Integer> thresholds, List<Champion> requiredChampions, UnitRegistry unitRegistry) {
        this.unitRegistry = unitRegistry;
        this.requiredUnits = requiredChampions.stream().map(unitRegistry::getUnitByChampion).collect(Collectors.toList());
        this.requiredTraits = requiredTraits;

        // init thresholds
        this.thresholds = IntStream.range(0, requiredTraits.size()).boxed().collect(Collectors.toMap(requiredTraits::get, thresholds::get));

        initTraits();
    }

    /**
     * Creates a composition from the constraints given in the constructor
     * @param nUnits Number of units to include in the composition
     * @return List of units
     */
    public List<Unit> createComp(int nUnits) {
        List<Unit> comp = new ArrayList<>(requiredUnits);

        for (int i=0; i<nUnits-requiredUnits.size(); i++){
            comp.add(this.getNextUnit());
        }

        this.visited.clear();
        this.currentTraits.clear();
        initTraits();

        return comp;
    }

    public Set<Unit> getVisited() {
        return Set.copyOf(visited);
    }

    public Map<Trait, Float> getCurrentTraits() {
        return Map.copyOf(currentTraits);
    }

    public List<Unit> getRequiredUnits() {
        return List.copyOf(requiredUnits);
    }

    public List<Trait> getRequiredTraits() {
        return List.copyOf(requiredTraits);
    }

    public Map<Trait, Integer> getThresholds() {
        return Map.copyOf(thresholds);
    }

    private void initTraits() {
        // populate current traits
        for (Unit unit : requiredUnits) {
            visited.add(unit);
            for (Trait trait : unit.getTraits()) {
                if (currentTraits.containsKey(trait)) {
                    currentTraits.replace(trait, currentTraits.get(trait) + 1.0f);
                } else {
                    currentTraits.put(trait, 1.0f);
                }
            }
        }

        for (Trait trait : requiredTraits) {
            if (!currentTraits.containsKey(trait)) {
                currentTraits.put(trait, 0f);
            }
        }

        // add traits that have initial thresholds of 1
        for (Trait trait : List.of(Trait.DIVINICORP, Trait.OVERLORD, Trait.GOD_OF_THE_NET, Trait.SOUL_KILLER, Trait.VIRUS)) {
            if (!currentTraits.containsKey(trait)) {
                currentTraits.put(trait, 0f);
            }
        }
    }

    private float generateEdgeWeight(Champion champion, Map<Trait, Float> currentTraits) {
        List<Trait> traits = unitRegistry.getUnitByChampion(champion).getTraits();
        float weight = 0f;

        for (Trait trait : traits) {
            if ((thresholds.containsKey(trait)) && (currentTraits.get(trait) < thresholds.get(trait))) {
                weight += 3f + Math.random();
            }

            if (currentTraits.containsKey(trait)) {
                if ((getNextThreshold(trait) - currentTraits.get(trait)) == 1) {
                    weight += 2 + Math.random();
                } else if ((getNextThreshold(trait) - currentTraits.get(trait)) > 1) {
                    weight += 1 + Math.random();
                }
            }
        }

        return weight;
    }

    private int getNextThreshold(Trait trait) {
        for (int threshold : trait.getThresholds()) {
            if (currentTraits.get(trait) < threshold) {
                return threshold;
            }
        }

        return trait.getThresholds()[trait.getThresholds().length - 1];
    }

    private Unit getNextUnit() {
        Set<Neighbors> tempNbs = new HashSet<>();

        // init priority q
        PriorityQueue<Neighbors> bestNeighbors = new PriorityQueue<>((a, b) -> Float.compare(b.getWeight(), a.getWeight()));

        // for all champions in each required trait
        for (Trait requiredTrait : currentTraits.keySet()) {
            for (Unit unit : unitRegistry.getUnitsByTrait(requiredTrait)) {
                if (!visited.contains(unit)) {
                    tempNbs.add(new Neighbors(unit.getChampion(), generateEdgeWeight(unit.getChampion(), currentTraits)));
                }
            }
        }

        bestNeighbors.addAll(tempNbs);

        Unit nextUnit = unitRegistry.getUnitByChampion(Objects.requireNonNull(bestNeighbors.poll()).getChampion());
        visited.add(nextUnit);
        updateCurrentTraitsWith(nextUnit);

        return nextUnit;
    }

    private void updateCurrentTraitsWith(Unit unit) {
        for (Trait trait : unit.getTraits()) {
            if (currentTraits.containsKey(trait)) {
                currentTraits.replace(trait, currentTraits.get(trait) + 1f);
            } else {
                currentTraits.put(trait, 1f);
            }
        }
    }
}
