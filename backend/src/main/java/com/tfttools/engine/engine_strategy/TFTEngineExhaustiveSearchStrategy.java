package com.tfttools.engine.engine_strategy;

import com.tfttools.domain.Composition;
import com.tfttools.engine.EngineState;
import com.tfttools.engine.engine_search.ExhaustiveSearchEngine;
import com.tfttools.engine.heuristic.*;

import java.util.List;

public class TFTEngineExhaustiveSearchStrategy implements TFTEngineStrategy {
    
    @Override
    public List<Composition> buildCompositions(StrategyContext context) {
        EngineState engineState = context.createEngineState();
        WeightRegistry registry = context.getWeightRegistry();

        // Create heuristic without diversity tie breaker since exhaustive search handles diversity naturally
        Heuristic heuristic = registry.builder()
                .with(
                        registry.createSynergyLookahead(engineState),
                        registry.createRequiredTraitsWeight(engineState),
                        registry.createRequiredUnitsWeight(engineState),
                        registry.createTraitsAddedWeightWithEmblems(engineState, registry.createEmblemWeightScorer(engineState))
                )
                .build();

        ExhaustiveSearchEngine exhaustiveEngine = new ExhaustiveSearchEngine(
                heuristic,
                engineState, 
                context.getEngineTerminatorManager()
        );

        List<Composition> allResults = exhaustiveEngine.buildCompositions();
        
        // Return exactly the number of compositions requested
        int requestedSize = context.getEngineConfiguration().getCompSize();
        return allResults.stream()
                .limit(requestedSize)
                .toList();
    }
}
