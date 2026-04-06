package com.tfttools.engine.heuristic.tiebreaker;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Unit;
import com.tfttools.engine.heuristic.tiebreaker.TieBreakerScorer;

import java.util.List;

public class DiversityTieBreaker implements TieBreakerScorer {
    private final List<Composition> previousCompositions;

    public DiversityTieBreaker(List<Composition> previousCompositions) {
        this.previousCompositions = previousCompositions;
    }

    @Override
    public int getScore(Unit unit) {
        // Higher score = less used in previous compositions
        long usageCount = previousCompositions.stream()
            .mapToLong(comp -> comp.getUnits().stream().mapToLong(u -> u.equals(unit) ? 1 : 0).sum())
            .sum();
        
        return (int) (1000 - usageCount);
    }

    @Override
    public int getPriority() {
        return 10;
    }
}
