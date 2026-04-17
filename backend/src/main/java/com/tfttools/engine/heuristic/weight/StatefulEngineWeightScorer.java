package com.tfttools.engine.heuristic.weight;

import com.tfttools.domain.Unit;

public interface StatefulEngineWeightScorer extends EngineWeightScorer
{
    void onUnitChosen(Unit unit);
}
