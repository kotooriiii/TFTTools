package com.tfttools.engine.manager;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.enginefilter.EngineFilter;
import com.tfttools.engine.enginefilter.ExcludedUnitFilter;
import com.tfttools.engine.enginefilter.ExcludedTraitFilter;
import com.tfttools.engine.enginefilter.RequiredUnitFilter;
import com.tfttools.registry.UnitRegistry;

import java.util.List;

public class EngineFilterManager {
    ExcludedUnitFilter excludedUnitFilter;
    ExcludedTraitFilter excludedTraitFilter;
    RequiredUnitFilter requiredUnitFilter;
    UnitRegistry unitRegistry;

    public EngineFilterManager(List<Unit> excludedUnits, List<Trait> excludedTraits, List<Unit> requiredUnits, UnitRegistry unitRegistry) {
        this.excludedUnitFilter = new ExcludedUnitFilter(excludedUnits, unitRegistry);
        this.excludedTraitFilter = new ExcludedTraitFilter(excludedTraits);
        this.requiredUnitFilter = new RequiredUnitFilter(requiredUnits, unitRegistry);
    }

    public List<EngineFilter> getEngineFilters() {
        return List.of(excludedUnitFilter, excludedTraitFilter, requiredUnitFilter);
    }
}
