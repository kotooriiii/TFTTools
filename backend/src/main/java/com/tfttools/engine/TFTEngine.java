package com.tfttools.engine;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Unit;
import com.tfttools.engine.engine_filter.EngineFilter;
import com.tfttools.engine.engine_strategy.StrategyContext;
import com.tfttools.engine.manager.EngineFilterManager;
import com.tfttools.engine.manager.EngineStrategyManager;
import com.tfttools.engine.manager.EngineTerminatorManager;
import com.tfttools.repository.UnitRepository;

import java.util.List;
import java.util.Set;

public class TFTEngine
{
    private final EngineConfiguration engineConfiguration;
    private final EngineTerminatorManager engineTerminatorManager;
    private final EngineFilterManager engineFilterManager;
    private final EngineStrategyManager engineStrategyManager;

    private final UnitRepository unitRepository;

    public TFTEngine(EngineConfiguration engineConfiguration, UnitRepository unitRepository)
    {
        this.engineConfiguration = engineConfiguration;
        this.unitRepository = unitRepository;


        this.engineTerminatorManager = new EngineTerminatorManager(
                engineConfiguration.getTactitionLevel(),
                engineConfiguration.getCrowns()
        );

        this.engineFilterManager = new EngineFilterManager(
                engineConfiguration.getExcludedUnits(),
                engineConfiguration.getExcludedTraits()
        );

        this.engineStrategyManager = new EngineStrategyManager();
    }

    /**
     * Apply all configured filters to the unit pool
     *
     * @return
     */
    private Set<Unit> applyFilters()
    {

        Set<Unit> allUnits = unitRepository.getAllUnits();

        List<EngineFilter> filters = engineFilterManager.getEngineFilters();

        for (EngineFilter engineFilter : filters)
        {
            engineFilter.filter(allUnits);
        }

        return allUnits;
    }


    /**
     * Builds compositions based on the engine configuration
     *
     * @return List of generated compositions
     */
    public List<Composition> buildCompositions()
    {
        Set<Unit> filteredUnitPool = applyFilters();

        StrategyContext strategyContext = new StrategyContext(
                engineConfiguration,
                engineTerminatorManager,
                unitRepository,
                filteredUnitPool);

        return engineStrategyManager.buildCompositions(
                engineConfiguration,
                strategyContext
        );
    }
}
