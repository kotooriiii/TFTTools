package com.tfttools.engine.engine_strategy;

import com.tfttools.domain.EngineConfiguration;
import com.tfttools.engine.heuristic.WeightRegistry;
import com.tfttools.engine.manager.*;
import com.tfttools.engine.EngineState;
import com.tfttools.registry.UnitRegistry;
import com.tfttools.domain.Composition;
import lombok.Getter;

@Getter
public class StrategyContext {
    private final EngineTerminatorManager engineTerminatorManager;
    private final EngineConfiguration engineConfiguration;
    private final WeightRegistry weightRegistry;

    public StrategyContext(EngineConfiguration engineConfiguration,
                           EngineTerminatorManager terminatorManager) {
        this.engineTerminatorManager = terminatorManager;
        this.engineConfiguration = engineConfiguration;
        this.weightRegistry = new WeightRegistry(engineConfiguration);

    }

    /**
     * Create a fresh EngineState for strategy use
     */
    public EngineState createEngineState() {
        return new EngineState(
                new Composition(),
                engineConfiguration.getRequiredUnits(),
                engineConfiguration.getEmblems(),
                engineConfiguration.getTactitionLevel()
        );
    }

}
