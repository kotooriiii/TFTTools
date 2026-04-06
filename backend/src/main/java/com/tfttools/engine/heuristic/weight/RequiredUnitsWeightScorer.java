package com.tfttools.engine.heuristic.weight;

import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;


public class RequiredUnitsWeightScorer implements EngineWeightScorer
{
    private final EngineState engineState;

    public RequiredUnitsWeightScorer(EngineState engineState)
    {
        this.engineState = engineState;
    }

    @Override
    public int getWeight(Unit unit)
    {
        int weight = 0;
        if (engineState.getRequiredUnits().contains(unit))
            weight = 1000;

        return weight;
    }
}
