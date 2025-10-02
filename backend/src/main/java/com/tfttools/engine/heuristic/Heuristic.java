package com.tfttools.engine.heuristic;

import com.tfttools.domain.Unit;
import com.tfttools.engine.heuristic.engineweight.LuckWeight;
import com.tfttools.engine.heuristic.engineweight.RequiredTraitsWeight;
import com.tfttools.engine.heuristic.engineweight.RequiredUnitsWeight;
import com.tfttools.engine.heuristic.engineweight.TraitsAddedWeight;

public class Heuristic {
    private final TraitsAddedWeight traitsAddedWeight;
    private final RequiredTraitsWeight requiredTraitsWeight;
    private final LuckWeight luckWeight;

    public Heuristic(TraitsAddedWeight traitsAddedWeight, RequiredTraitsWeight requiredTraitsWeight, LuckWeight luckWeight) {
        this.traitsAddedWeight = traitsAddedWeight;
        this.requiredTraitsWeight = requiredTraitsWeight;
        this.luckWeight = luckWeight;
    }

    public int getWeight(Unit unit) {
        return traitsAddedWeight.getWeight(unit) + requiredTraitsWeight.getWeight(unit) + luckWeight.getWeight(unit);
    }
}
