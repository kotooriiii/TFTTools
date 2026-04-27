package com.tfttools.engine.heuristic;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.engine.manager.EngineTerminatorManager;

public class CompositionBuilder {
    private final EngineState engineState;
    private final EngineTerminatorManager terminatorManager;

    public CompositionBuilder(EngineState engineState, EngineTerminatorManager terminatorManager) {
        this.engineState = engineState;
        this.terminatorManager = terminatorManager;
    }

    /**
     * Build a composition using the given unit picker
     */
    public Composition buildWith(UnitSelector unitSelector) {
        while (!terminatorManager.shouldTerminate(engineState.getCurrentComp()) 
               && engineState.hasUnitsAvailable()) {

            unitSelector.pickNextUnit(engineState);
        }

        return engineState.getCurrentComp();
    }
}
