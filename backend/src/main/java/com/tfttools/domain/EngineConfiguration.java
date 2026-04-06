package com.tfttools.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
@Builder
public class EngineConfiguration
{
    private final int compSize;
    private final Map<Trait, Integer> requiredTraits;
    private final Set<Unit> requiredUnits;
    private final Set<Trait> excludedTraits;
    private final Set<Unit> excludedUnits;
    private final int costOfBoard;
    private final int tactitionLevel;
    private final int crowns;
    private final Set<Emblem> emblems;
    private final float luck;

    public int getMaxUnitsOnBoard()
    {
        return tactitionLevel + crowns;
    }

    public boolean hasRequiredUnits()
    {
        return !requiredUnits.isEmpty();
    }

    public boolean hasRequiredTraits()
    {
        return !requiredTraits.isEmpty();
    }

    public boolean hasEmblems()
    {
        return !emblems.isEmpty();
    }
}
