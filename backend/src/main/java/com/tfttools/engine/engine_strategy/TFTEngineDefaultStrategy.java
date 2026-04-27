package com.tfttools.engine.engine_strategy;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.engine.heuristic.CompositionBuilder;
import com.tfttools.engine.heuristic.UnitSelector;
import com.tfttools.engine.heuristic.Heuristic;
import com.tfttools.engine.heuristic.WeightRegistry;

import java.util.*;

public class TFTEngineDefaultStrategy implements TFTEngineStrategy
{
    @Override
    public List<Composition> buildCompositions(StrategyContext context)
    {
        List<Composition> compositions = new ArrayList<>();

        for (int i = 0; i < context.getEngineConfiguration().getCompSize(); i++)
        {
            Composition comp = buildComposition(context, compositions);
            compositions.add(comp);
        }

        return compositions;
    }

    private Composition buildComposition(StrategyContext context, List<Composition> previousComps)
    {
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

        UnitSelector unitSelector = new UnitSelector(heuristic);
        CompositionBuilder compositionBuilder = new CompositionBuilder(engineState, context.getEngineTerminatorManager());

        return compositionBuilder.buildWith(unitSelector);
    }
}
