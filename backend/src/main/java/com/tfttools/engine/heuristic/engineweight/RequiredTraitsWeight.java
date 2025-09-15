package com.tfttools.engine.heuristic.engineweight;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;

import java.util.Map;

public class RequiredTraitsWeight implements EngineWeight{
    private final EngineState engineState;
    private final Map<Trait, Integer> currentTraits;
    private final Map<Trait, Integer> requiredTraits;
    private int weight;

    public RequiredTraitsWeight(EngineState engineState, Map<Trait, Integer> requiredTraits) {
        this.engineState = engineState;
        this.currentTraits = engineState.getCurrentTraits();
        this.requiredTraits = requiredTraits;
        this.weight = 0;
    }

    @Override
    public int getWeight(Unit unit) {
        for (Trait trait : unit.getTraits()) {
            if (requiredTraits.containsKey(trait)) {
                if (currentTraits.get(trait) < requiredTraits.get(trait)) {
                    weight += 3;
                }
            }
        }
        return weight;
    }
}
