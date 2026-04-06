package com.tfttools.engine.heuristic.weight;

import com.tfttools.domain.Unit;

public class CostWeightScorer implements EngineWeightScorer
{
    @Override
    public int getWeight(Unit unit) {
        return unit.getCost();
    }
}
