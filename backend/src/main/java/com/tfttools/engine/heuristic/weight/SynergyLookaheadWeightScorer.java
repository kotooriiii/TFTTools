package com.tfttools.engine.heuristic.weight;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.repository.UnitRepository;
import com.tfttools.util.CompositionUtils;

import java.util.*;

public class SynergyLookaheadWeightScorer implements StatefulEngineWeightScorer
{
    private static final int MAX_DEPTH = 2;

    private final EngineState engineState;

    private final Map<Unit, Integer> synergyWeight;

    private final UnitRepository unitRepository;

    public SynergyLookaheadWeightScorer(EngineState engineState, UnitRepository unitRepository)
    {
        this.engineState = engineState;
        this.unitRepository = unitRepository;
        this.synergyWeight = new HashMap<>();

    }

    @Override
    public void onUnitChosen(Unit unit)
    {
        if (engineState.getCurrentComp().getUnits().isEmpty())
            return;

        if (engineState.getCurrentComp().getUnits().size() >= 2)
            return;

        synergyWeight.clear();

        Composition currentComp = engineState.getCurrentComp();

        List<Unit> currentUnitsAndCandidate = new ArrayList<>(currentComp.getUnits());
        currentUnitsAndCandidate.add(unit);

        List<Trait> activatableTraits = CompositionUtils.INSTANCE.getTraitsReachingFirstThresholdWhenUnitAdded(currentComp, unit);

        // Note : Units with one trait that activate immediately (think TFTSet16 Targon), would not be added here. Which is fine, because getWeight(Unit) only works if a trait was activated.
        List<Unit> synergies = getSynergies(currentUnitsAndCandidate, activatableTraits, MAX_DEPTH);

        synergies.forEach(synergyUnit -> synergyWeight.put(synergyUnit, synergies.size()));

    }

    @Override
    public int getWeight(Unit unit)
    {
        Composition currentComp = engineState.getCurrentComp();

        List<Trait> activatableTraits = CompositionUtils.INSTANCE.getTraitsReachingFirstThresholdWhenUnitAdded(currentComp, unit);

        //I suppose the idea here that if we are adding a unit in, we want at least 2 (subject to change to other criteria) units in the composition before we start thinking about synergies
        if (currentComp.getUnits().isEmpty()) return 0;

        //If there is no trait being activated, we can skip.
        // If there is, and the unit we are trying to activate was not in our synergy map, then skip it.
        if (activatableTraits.isEmpty() || (!synergyWeight.isEmpty() && !synergyWeight.containsKey(unit)))
            return 0;

        List<Unit> currentUnitsAndCandidate = new ArrayList<>(currentComp.getUnits());
        currentUnitsAndCandidate.add(unit);

        activatableTraits.addAll(CompositionUtils.INSTANCE.getActivatedTraits(engineState.getCurrentComp()));

        List<Unit> synergies = getSynergies(currentUnitsAndCandidate, activatableTraits, MAX_DEPTH);
        return synergyWeight.isEmpty() ? synergies.size() : synergyWeight.getOrDefault(unit, 0);
    }

    public List<Unit> getSynergies(List<Unit> units, List<Trait> excludedTraits, int depth)
    {
        if (depth <= 0)
        {
            return Collections.emptyList();
        }

        Set<Trait> visitedTraits = new HashSet<>(excludedTraits);

        List<Trait> uniqueTraits = units.stream().flatMap(unit -> unit.getTraits().stream()).distinct().toList();
        Queue<Trait> currentLevel = new LinkedList<>(uniqueTraits);

        Composition composition = new Composition();

        for (int currentDepth = 0; currentDepth < depth && !currentLevel.isEmpty(); currentDepth++)
        {
            Queue<Trait> nextLevel = new LinkedList<>();

            List<Unit> attemptedUnits = new ArrayList<>();

            while (!currentLevel.isEmpty())
            {
                Trait trait = currentLevel.poll();

                if (!visitedTraits.add(trait))
                {
                    continue; // Skip if already visited
                }

                List<Unit> unitsWithTrait = unitRepository.getUnitsByTrait(trait, engineState.getUnitPool());
                attemptedUnits.addAll(unitsWithTrait);
            }

            composition = new Composition(new ArrayList<>(attemptedUnits));
            CompositionUtils.INSTANCE.reduceCompositionToSynergies(composition);

            // Add traits from units to next level
            composition.getUnits().stream()
                    .flatMap(unit -> unit.getTraits().stream())
                    .filter(t -> !visitedTraits.contains(t))
                    .forEach(nextLevel::add);

            currentLevel = nextLevel;
        }


        return composition.getUnits();
    }
}