package com.tfttools.engine.heuristic.weight;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;

import java.util.Map;

public class RequiredTraitsWeightScorer implements EngineWeightScorer
{
    private final EngineState engineState;
    private final Map<Trait, Integer> requiredTraits;
    private int weight;

    public RequiredTraitsWeightScorer(EngineState engineState, Map<Trait, Integer> requiredTraits) {
        this.engineState = engineState;
        this.requiredTraits = requiredTraits;
        this.weight = 0;
    }

    @Override
    public int getWeight(Unit unit) {
        this.weight = 0;
        for (Trait trait : unit.getTraits()) {
            if (requiredTraits.containsKey(trait)) {
                if (engineState.getCurrentComp().getTraits().getOrDefault(trait, 0) < requiredTraits.get(trait)) {
                    weight += 10;
                }
            }
        }
        return weight;
    }
}
