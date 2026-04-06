package com.tfttools.engine.heuristic;

import com.tfttools.domain.Unit;
import com.tfttools.engine.heuristic.tiebreaker.TieBreakerScorer;
import com.tfttools.engine.heuristic.weight.EngineWeightScorer;
import lombok.Getter;

import java.util.List;

@Getter
public class Heuristic {
    private final List<EngineWeightScorer> weights;
    private final List<TieBreakerScorer> tieBreakers;

    public Heuristic(List<EngineWeightScorer> weights, List<TieBreakerScorer> tieBreakers) {
        this.weights = weights;
        this.tieBreakers = tieBreakers;
    }

    public int getWeight(Unit unit) {
        return weights.stream()
                .mapToInt(weight -> weight.getWeight(unit))
                .sum();
    }
}