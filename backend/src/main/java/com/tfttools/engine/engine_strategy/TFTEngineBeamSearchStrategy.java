package com.tfttools.engine.engine_strategy;

import com.tfttools.domain.Composition;
import com.tfttools.engine.EngineState;
import com.tfttools.engine.engine_search.BeamSearchEngine;
import com.tfttools.engine.heuristic.*;

import java.util.List;

public class TFTEngineBeamSearchStrategy implements TFTEngineStrategy {

    private final Integer customBeamWidth;

    public TFTEngineBeamSearchStrategy() {
        this.customBeamWidth = null;
    }

    public TFTEngineBeamSearchStrategy(int beamWidth) {
        this.customBeamWidth = beamWidth;
    }

    @Override
    public List<Composition> buildCompositions(StrategyContext context)
    {
        EngineState engineState = context.createEngineState();
        WeightRegistry registry = context.getWeightRegistry();

        // Use custom beam width or fall back to number of requested compositions
        int beamWidth = customBeamWidth != null ?
                customBeamWidth :
                context.getEngineConfiguration().getCompSize();


        // Create heuristic without diversity tie breaker since beam search handles diversity naturally
        Heuristic heuristic = registry.builder()
                .with(
                        registry.createSynergyLookahead(engineState),
                        registry.createRequiredTraitsWeight(engineState),
                        registry.createRequiredUnitsWeight(engineState),
                        registry.createTraitsAddedWeightWithEmblems(engineState, registry.createEmblemWeightScorer(engineState))
                )
                .build();

        BeamSearchEngine beamSearchEngine = new BeamSearchEngine(
                heuristic,
                engineState,
                context.getEngineTerminatorManager(),
                beamWidth
        );

        List<Composition> beamResults = beamSearchEngine.buildCompositions();

        // Return exactly the number of compositions requested
        int requestedSize = context.getEngineConfiguration().getCompSize();
        return beamResults.stream()
                .limit(requestedSize)
                .toList();
    }
}
