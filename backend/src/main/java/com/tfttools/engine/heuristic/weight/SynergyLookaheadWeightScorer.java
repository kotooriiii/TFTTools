package com.tfttools.engine.heuristic.weight;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.util.CompositionUtils;

import java.util.*;

public class SynergyLookaheadWeightScorer implements StatefulEngineWeightScorer
{
    private final EngineState engineState;
    private static final int MAX_DEPTH = 2;
    private final Set<Unit> unitPool;
    private final Map<Trait, List<Unit>> traitToUnits;

    private final Map<Unit, Integer> synergyWeight; //todo should be something stateful ?

    public SynergyLookaheadWeightScorer(EngineState engineState, Set<Unit> unitPool)
    {
        this.engineState = engineState;
        this.unitPool = unitPool;
        this.traitToUnits = new HashMap<>();
        this.synergyWeight = new HashMap<>();

        for (Unit unit : unitPool)
        {
            for (Trait trait : unit.getTraits())
            {
                this.traitToUnits.computeIfAbsent(trait, k -> new ArrayList<>()).add(unit);
            }
        }

    }//todo cost value

    @Override
    public void onUnitChosen(Unit unit)
    {
        if(engineState.getCurrentComp().getUnits().isEmpty())
            return;

        if(engineState.getCurrentComp().getUnits().size() >= 2)
            return;

        synergyWeight.clear();

        Composition currentComp = engineState.getCurrentComp();

        List<Unit> currentUnitsAndCandidate = new ArrayList<>(currentComp.getUnits());
        currentUnitsAndCandidate.add(unit);

        List<Trait> activatableTraits = CompositionUtils.getTraitsReachingFirstThresholdWhenUnitAdded(currentComp, unit);

        List<Unit> synergies = getSynergies(currentUnitsAndCandidate, activatableTraits, MAX_DEPTH); //todo targons dont get added in synergies.. should they.. thye wont ever make it in thats why

        synergies.forEach(synergyUnit -> synergyWeight.put(synergyUnit, synergies.size()));

    }

    @Override
    public int getWeight(Unit unit)
    {
        Composition currentComp = engineState.getCurrentComp();

        List<Trait> activatableTraits = CompositionUtils.getTraitsReachingFirstThresholdWhenUnitAdded(currentComp, unit);

        if (currentComp.getUnits().isEmpty()) return 0; //todo maybe ? should it work empty. undefined behavior atm

        if(activatableTraits.isEmpty()  ||  (!synergyWeight.isEmpty() && !synergyWeight.containsKey(unit))) //todo could be activtable is targon, but it was not in the map
            return 0;

        //todo somewhere inthe code need to wrry about exclusion
        List<Unit> currentUnitsAndCandidate = new ArrayList<>(currentComp.getUnits());
        currentUnitsAndCandidate.add(unit);

        activatableTraits.addAll(CompositionUtils.getActivatedTraits(engineState.getCurrentComp()));

        List<Unit> synergies = getSynergies(currentUnitsAndCandidate, activatableTraits, MAX_DEPTH);
        return synergyWeight.isEmpty() ? synergies.size() : synergyWeight.getOrDefault(unit, 0);
    }

    public List<Unit> getSynergies(List<Unit> units, List<Trait> excludedTraits, int depth) {
        if (depth <= 0) {
            return Collections.emptyList();
        }

        Set<Unit> allUnits = new HashSet<>();
        Set<Trait> visitedTraits = new HashSet<>(excludedTraits);

        List<Trait> uniqueTraits = units.stream().flatMap(unit -> unit.getTraits().stream()).distinct().toList();
        Queue<Trait> currentLevel = new LinkedList<>(uniqueTraits);

        Composition composition = new Composition();

        for (int currentDepth = 0; currentDepth < depth && !currentLevel.isEmpty(); currentDepth++) {
            Queue<Trait> nextLevel = new LinkedList<>();

            List<Unit> attemptedUnits = new ArrayList<>();

            while (!currentLevel.isEmpty()) {
                Trait trait = currentLevel.poll();

                if (!visitedTraits.add(trait)) {
                    continue; // Skip if already visited
                }

                List<Unit> unitsWithTrait = getUnitsByTrait(trait);
                attemptedUnits.addAll(unitsWithTrait);

//todo make sure if the triat == 1, amke sure that the unit is not completing any other of its traits.
            }

            composition = new Composition(new ArrayList<>(attemptedUnits));
            CompositionUtils compositionUtils = new CompositionUtils();
            compositionUtils.reduceCompositionToSynergies(composition);

            allUnits.addAll(composition.getUnits());

            // Add traits from units to next level
            composition.getUnits().stream()
                    .flatMap(unit -> unit.getTraits().stream())
                    .filter(t -> !visitedTraits.contains(t))
                    .forEach(nextLevel::add);

            currentLevel = nextLevel;
        }


        return composition.getUnits();
    }

    private List<Unit> getUnitsByTrait(Trait trait)
    {
        return traitToUnits.getOrDefault(trait, Collections.emptyList());
    }

}
