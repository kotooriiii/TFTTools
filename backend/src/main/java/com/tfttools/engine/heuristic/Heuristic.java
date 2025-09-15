package com.tfttools.engine.heuristic;

import com.tfttools.domain.Unit;
import com.tfttools.engine.heuristic.engineweight.LuckWeight;
import com.tfttools.engine.heuristic.engineweight.RequiredTraitsWeight;
import com.tfttools.engine.heuristic.engineweight.RequiredUnitsWeight;
import com.tfttools.engine.heuristic.engineweight.TraitsAddedWeight;

public class Heuristic {
    TraitsAddedWeight traitsAddedWeight;
    RequiredUnitsWeight requiredUnitsWeight;
    RequiredTraitsWeight requiredTraitsWeight;
    LuckWeight luckWeight;

    public Heuristic(TraitsAddedWeight traitsAddedWeight, RequiredUnitsWeight requiredUnitsWeight, RequiredTraitsWeight requiredTraitsWeight, LuckWeight luckWeight) {
        this.traitsAddedWeight = traitsAddedWeight;
        this.requiredUnitsWeight = requiredUnitsWeight;
        this.requiredTraitsWeight = requiredTraitsWeight;
        this.luckWeight = luckWeight;
    }

    public Heuristic(TraitsAddedWeight traitsAddedWeight, RequiredTraitsWeight requiredTraitsWeight, LuckWeight luckWeight) {
        this.traitsAddedWeight = traitsAddedWeight;
        this.requiredTraitsWeight = requiredTraitsWeight;
        this.luckWeight = luckWeight;
    }

    public int getWeight(Unit unit) {
        return traitsAddedWeight.getWeight(unit) + requiredTraitsWeight.getWeight(unit) + luckWeight.getWeight(unit);
    }
}
