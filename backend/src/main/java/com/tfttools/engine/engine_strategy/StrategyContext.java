package com.tfttools.engine.engine_strategy;

import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Unit;
import com.tfttools.engine.heuristic.WeightRegistry;
import com.tfttools.engine.manager.*;
import com.tfttools.engine.EngineState;
import com.tfttools.domain.Composition;
import com.tfttools.repository.UnitRepository;
import lombok.Getter;

import java.util.Set;

@Getter
public class StrategyContext {
    private final EngineTerminatorManager engineTerminatorManager;
    private final EngineConfiguration engineConfiguration;
    private final WeightRegistry weightRegistry;

    private final Set<Unit> unitPool;

    public StrategyContext(EngineConfiguration engineConfiguration,
                           EngineTerminatorManager terminatorManager, UnitRepository unitRepository, Set<Unit> unitPool) {
        this.engineConfiguration = engineConfiguration;
        this.engineTerminatorManager = terminatorManager;
        this.unitPool = unitPool;

        this.weightRegistry = new WeightRegistry(engineConfiguration, unitRepository);

    }

    /**
     * Create a fresh EngineState for strategy use
     */
    public EngineState createEngineState() {
        return new EngineState(
                new Composition(),
                engineConfiguration,
                unitPool
        );
    }

}
