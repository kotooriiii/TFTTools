package com.tfttools.engine;

import com.tfttools.domain.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class EngineState
{
    private final Composition currentComp;
    private final EngineConfiguration engineConfiguration;

    private final Set<Unit> unitPool;

    public EngineState(Composition comp, EngineConfiguration engineConfiguration, Set<Unit> unitPool)
    {
        this.currentComp = comp;
        this.engineConfiguration = engineConfiguration;
        this.unitPool = unitPool;
    }

    /**
     * Check if any units are still available
     */
    public boolean hasUnitsAvailable() {
        return !getUnitPool().isEmpty();
    }

    /**
     * Get count of remaining units
     */
    public int getRemainingCount() {
        return getUnitPool().size();
    }
}
