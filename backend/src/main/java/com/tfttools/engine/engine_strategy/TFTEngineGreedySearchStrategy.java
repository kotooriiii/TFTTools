package com.tfttools.engine.engine_strategy;

import com.tfttools.domain.Composition;
import com.tfttools.engine.EngineState;
import com.tfttools.engine.engine_search.GreedySearchEngine;
import com.tfttools.engine.heuristic.*;

import java.util.*;

public class TFTEngineGreedySearchStrategy implements TFTEngineStrategy {

    @Override
    public List<Composition> buildCompositions(StrategyContext context) {
        List<Composition> compositions = new ArrayList<>();

        for (int i = 0; i < context.getEngineConfiguration().getCompSize(); i++) {
            List<Composition> newComps = buildComposition(context, compositions);
            compositions.addAll(newComps);
        }

        return compositions;
    }

    private List<Composition> buildComposition(StrategyContext context, List<Composition> previousComps) {
        EngineState engineState = context.createEngineState();
        WeightRegistry registry = context.getWeightRegistry();

        Heuristic heuristic = registry.builder()
                .with(
                        registry.createSynergyLookahead(engineState),
                        registry.createRequiredTraitsWeight(engineState),
                        registry.createRequiredUnitsWeight(engineState),
                        registry.createTraitsAddedWeightWithEmblems(engineState, registry.createEmblemWeightScorer(engineState))
                )
                .withTieBreaker(
                        registry.createDiversityTieBreaker(previousComps)
                )
                .build();

        GreedySearchEngine greedySearchEngine = new GreedySearchEngine(heuristic, engineState, context.getEngineTerminatorManager());
        return greedySearchEngine.buildCompositions();
    }
}