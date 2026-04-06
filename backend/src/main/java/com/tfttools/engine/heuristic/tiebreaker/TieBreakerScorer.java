package com.tfttools.engine.heuristic.tiebreaker;

import com.tfttools.engine.heuristic.UnitScorer;

/**
 * Marker interface for tiebreaker scorers
 */
public interface TieBreakerScorer extends UnitScorer
{
    /**
     * Priority of this tiebreaker (lower = higher priority)
     */
    int getPriority();
}
