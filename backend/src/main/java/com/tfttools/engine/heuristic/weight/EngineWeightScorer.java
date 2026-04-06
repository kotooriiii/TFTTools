package com.tfttools.engine.heuristic.weight;

import com.tfttools.domain.Unit;
import com.tfttools.engine.heuristic.UnitScorer;

public interface EngineWeightScorer extends UnitScorer
{
    @Override
    default int getScore(Unit unit)
    {
        return getWeight(unit);
    }

    int getWeight(Unit unit);
}
