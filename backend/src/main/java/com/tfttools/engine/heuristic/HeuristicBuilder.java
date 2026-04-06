package com.tfttools.engine.heuristic;

import com.tfttools.engine.heuristic.tiebreaker.TieBreakerScorer;
import com.tfttools.engine.heuristic.weight.EngineWeightScorer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HeuristicBuilder {
    private final List<EngineWeightScorer> weights = new ArrayList<>();
    private final List<TieBreakerScorer> tieBreakers = new ArrayList<>();

    public HeuristicBuilder with(EngineWeightScorer... weights) {
        this.weights.addAll(Arrays.asList(weights));
        return this;
    }

    public HeuristicBuilder withTieBreaker(TieBreakerScorer... tieBreakers) {
        this.tieBreakers.addAll(Arrays.asList(tieBreakers));
        return this;
    }

    public Heuristic build() {
        // Sort tiebreakers by priority
        List<TieBreakerScorer> sortedTieBreakers = tieBreakers.stream()
            .sorted(Comparator.comparingInt(TieBreakerScorer::getPriority))
            .collect(Collectors.toList());

        return new Heuristic(weights, sortedTieBreakers);
    }
}
