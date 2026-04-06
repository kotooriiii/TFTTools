package com.tfttools.engine;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Emblem;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.registry.UnitRegistry;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class EngineState
{
    private final Composition currentComp;
    private final Set<Emblem> availableEmblems;
    private final Set<Unit> requiredUnits;
    private final int tactitionLevel;

    public EngineState(Composition comp, Set<Unit> requiredUnits, Set<Emblem> availableEmblems, int tactitionLevel)
    {
        this.currentComp = comp;
        this.requiredUnits = requiredUnits;
        this.availableEmblems = availableEmblems;
        this.tactitionLevel = tactitionLevel;
    }
}
