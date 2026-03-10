package com.tfttools.engine.manager;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.engine.heuristic.Heuristic;
import com.tfttools.engine.heuristic.engineweight.LuckWeight;
import com.tfttools.engine.heuristic.engineweight.RequiredTraitsWeight;
import com.tfttools.engine.heuristic.engineweight.TraitsAddedWeight;

import java.util.List;
import java.util.Map;

public class EngineHeuristicManager {
    private final EngineState engineState;
    private final Heuristic heuristic;

    public EngineHeuristicManager(List<Unit> requiredUnits, Map<Trait, Integer> requiredTraits, float luck, List<Trait> emblems, int costOfBoard, EngineState engineState) {
        this.engineState = engineState;

        TraitsAddedWeight traitsAddedWeight = new TraitsAddedWeight(engineState);
//        RequiredUnitsWeight requiredUnitsWeight = new RequiredUnitsWeight(engineState, requiredChampions);
        RequiredTraitsWeight requiredTraitsWeight = new RequiredTraitsWeight(engineState, requiredTraits);
        LuckWeight luckWeight = new LuckWeight(engineState, luck);

        this.heuristic = new Heuristic(traitsAddedWeight, requiredTraitsWeight, luckWeight);
    }

    public Heuristic getHeuristic() {
        return heuristic;
    }
}
