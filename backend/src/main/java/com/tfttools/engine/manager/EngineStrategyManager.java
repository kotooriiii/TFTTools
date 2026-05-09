package com.tfttools.engine.manager;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Trait;
import com.tfttools.engine.engine_strategy.StrategyContext;
import com.tfttools.engine.engine_strategy.TFTEngineBeamSearchStrategy;
import com.tfttools.engine.engine_strategy.TFTEngineStrategy;

import java.util.*;

public class EngineStrategyManager
{
    private final List<TFTEngineStrategy> strategies;
    private final CompositionValidationManager validationManager;

    public EngineStrategyManager()
    {
        this.strategies = List.of(
                new TFTEngineBeamSearchStrategy(12)
                //new TFTEngineDefaultStrategy()
                // Add more strategies here
        );
        this.validationManager = new CompositionValidationManager();

    }

    public List<Composition> buildCompositions(EngineConfiguration engineConfiguration,
                                               StrategyContext context)
    {
        List<Composition> allStrategyComps = new ArrayList<>();

        this.strategies.forEach(strategy ->
        {
            List<Composition> comps = strategy.buildCompositions(context);
            allStrategyComps.addAll(comps);
        });

        List<Composition> validCompositions = validationManager.filterValidCompositions(allStrategyComps, engineConfiguration);


        return rankAndDeduplicate(validCompositions, engineConfiguration);
    }

    private List<Composition> rankAndDeduplicate(List<Composition> compositions,
                                                 EngineConfiguration engineConfiguration)
    {
        // Remove duplicates and rank by some criteria
        return compositions.stream()
                .distinct()
                .sorted(Comparator.comparing(this::evaluateComposition).reversed())
                .limit(engineConfiguration.getCompSize())
                .toList();
    }

    private double evaluateComposition(Composition comp)
    {
        //todo maybe also eval 'strength/tankiness_vs_dps'
        return comp.getTraits().entrySet().stream().filter(traitEntry ->
        {
            Trait trait = traitEntry.getKey();
            int currentTraitCount = traitEntry.getValue();
            return trait.isActivated(currentTraitCount);
        }).toList().size();
    }
}
