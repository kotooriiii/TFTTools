package com.tfttools.engine.manager;

import com.tfttools.domain.Champion;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.registry.UnitRegistry;

import java.util.List;
import java.util.Set;

public class EngineStateManager {
    private final EngineState engineState;

    public EngineStateManager(Set<Unit> comp, List<Champion> requiredChampions, List<Trait> availableEmblems, int tactitionLevel, UnitRegistry unitRegistry) {
        engineState = new EngineState(comp, requiredChampions, availableEmblems, tactitionLevel, unitRegistry);
    }

    public EngineState getEngineState() {
        return engineState;
    }
}
