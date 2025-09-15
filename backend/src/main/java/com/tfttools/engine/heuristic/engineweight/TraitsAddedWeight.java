package com.tfttools.engine.heuristic.engineweight;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;

import java.util.Map;

public class TraitsAddedWeight implements EngineWeight{
    private final EngineState engineState;
    private final Map<Trait, Integer> currentTraits;
    private int weight;

    public TraitsAddedWeight(EngineState engineState) {
        this.engineState = engineState;
        this.currentTraits = engineState.getCurrentTraits();
        this.weight = 0;
    }

    @Override
    public int getWeight(Unit unit) {
        this.weight = 0;

        for (Trait trait : unit.getTraits()) {
            if ((getNextThreshold(trait) - currentTraits.get(trait)) == 1) {
                weight += 4;
            } else if ((getNextThreshold(trait) - currentTraits.get(trait)) > 1) {
                weight += 2;
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
}
