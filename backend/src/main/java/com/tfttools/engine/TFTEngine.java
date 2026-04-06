package com.tfttools.engine;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Unit;
import com.tfttools.engine.engine_strategy.StrategyContext;
import com.tfttools.engine.engine_filter.EngineFilter;
import com.tfttools.engine.manager.EngineFilterManager;
import com.tfttools.engine.manager.EngineStrategyManager;
import com.tfttools.engine.manager.EngineTerminatorManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TFTEngine
{
    private final EngineConfiguration engineConfiguration;
    private final EngineTerminatorManager engineTerminatorManager;
    private final EngineFilterManager engineFilterManager;
    private final EngineStrategyManager engineStrategyManager;

    private final List<Unit> unitPool;

    public TFTEngine(EngineConfiguration engineConfiguration, List<Unit> unitPool) {
        this.engineConfiguration = engineConfiguration;


        this.engineTerminatorManager = new EngineTerminatorManager(
                engineConfiguration.getTactitionLevel(),
                engineConfiguration.getCrowns()
        );

        this.engineFilterManager = new EngineFilterManager(
                engineConfiguration.getExcludedUnits(),
                engineConfiguration.getExcludedTraits()
        );

        this.engineStrategyManager = new EngineStrategyManager();

        // Initialize unit pool
        this.unitPool = new ArrayList<>(unitPool);
    }

    /**
     * Apply all configured filters to the unit pool
     */
    private void applyFilters() {
        List<EngineFilter> filters = engineFilterManager.getEngineFilters();

        for (EngineFilter engineFilter : filters) {
            engineFilter.filter(unitPool);
        }
    }



    /**
     * Builds compositions based on the engine configuration
     *
     * @return List of generated compositions
     */
    public List<Composition> buildCompositions() {
        applyFilters();

        StrategyContext strategyContext =  new StrategyContext(
                engineConfiguration,
                engineTerminatorManager
        );

        return engineStrategyManager.buildCompositions(
                new HashSet<>(unitPool),
                engineConfiguration,
                strategyContext
        );
    }
}
