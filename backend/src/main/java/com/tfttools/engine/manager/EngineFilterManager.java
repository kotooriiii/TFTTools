package com.tfttools.engine.manager;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.enginefilter.EngineFilter;
import com.tfttools.engine.enginefilter.ExcludedUnitFilter;
import com.tfttools.engine.enginefilter.ExcludedTraitFilter;
import com.tfttools.engine.enginefilter.RequiredUnitFilter;
import com.tfttools.registry.UnitRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EngineFilterManager {
    private final ExcludedUnitFilter excludedUnitFilter;
    private final ExcludedTraitFilter excludedTraitFilter;
    private final RequiredUnitFilter requiredUnitFilter;
    private final UnitRegistry unitRegistry;

    public EngineFilterManager(List<Unit> excludedUnits, List<Trait> excludedTraits, List<Unit> requiredUnits, UnitRegistry unitRegistry) {
        this.excludedUnitFilter = new ExcludedUnitFilter(excludedUnits, unitRegistry);
        this.excludedTraitFilter = new ExcludedTraitFilter(excludedTraits);
        this.requiredUnitFilter = new RequiredUnitFilter(requiredUnits, unitRegistry);
        this.unitRegistry = unitRegistry;
    }

    public List<EngineFilter> getEngineFilters() {
        return List.of(excludedUnitFilter, excludedTraitFilter, requiredUnitFilter);
    }
}
