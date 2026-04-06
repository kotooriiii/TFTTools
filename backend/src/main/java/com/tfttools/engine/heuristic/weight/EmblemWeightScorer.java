package com.tfttools.engine.heuristic.weight;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Emblem;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;

import java.util.*;

public class EmblemWeightScorer implements EngineWeightScorer {
    private final EngineState engineState;
    private final List<Emblem> availableEmblems;

    public EmblemWeightScorer(EngineState engineState, List<Emblem> availableEmblems) {
        this.engineState = engineState;
        this.availableEmblems = availableEmblems;
    }

    @Override
    public int getWeight(Unit unit) {
        return getBestEmblemForUnit(unit)
                .map(emblem -> calculateEmblemValue(unit, emblem))
                .orElse(0);
    }

    // New method to expose the best emblem for a unit
    public Optional<Emblem> getBestEmblemForUnit(Unit unit) {
        return availableEmblems.stream()
                .filter(emblem -> canReceiveEmblem(unit, emblem))
                .max(Comparator.comparingInt(emblem -> calculateEmblemValue(unit, emblem)));
    }


    private boolean canReceiveEmblem(Unit unit, Emblem emblem) {
        // Unit can't already have this trait
        return !unit.getTraits().contains(emblem.getTrait());
    }

    private int calculateEmblemValue(Unit unit, Emblem emblem) {
        // Calculate how much value adding this emblem would provide
        Composition tempComp = new Composition(engineState.getCurrentComp());
        tempComp.add(unit);

        // Simulate adding the emblem trait
        Trait emblemTrait = emblem.getTrait();
        int currentCount = tempComp.getTraits().getOrDefault(emblemTrait, 0);

        // Check if this would activate or improve a trait threshold
        if (emblemTrait.isActivated(currentCount + 1) &&
                !emblemTrait.isActivated(currentCount)) {
            return 100; // High value for activating new trait
        }

        return 10; // Lower value for just adding to existing trait
    }
}