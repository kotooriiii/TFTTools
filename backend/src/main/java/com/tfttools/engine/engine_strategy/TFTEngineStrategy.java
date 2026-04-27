package com.tfttools.engine.engine_strategy;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Unit;

import java.util.List;
import java.util.Set;

public interface TFTEngineStrategy
{
    /**
     * Build a single composition using this strategy
     * @param strategyContext Shared context between strategies
     * @return A completed composition
     */
    List<Composition> buildCompositions(StrategyContext strategyContext);

}
