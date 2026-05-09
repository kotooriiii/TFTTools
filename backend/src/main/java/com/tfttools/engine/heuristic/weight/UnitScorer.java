package com.tfttools.engine.heuristic.weight;

import com.tfttools.domain.Unit;

/**
 * Common interface for all unit scoring mechanisms
 */
public interface UnitScorer {
    int getScore(Unit unit);
}
