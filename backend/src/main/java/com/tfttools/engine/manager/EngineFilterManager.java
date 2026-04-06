package com.tfttools.engine.manager;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.engine_filter.EngineFilter;
import com.tfttools.engine.engine_filter.ExcludedUnitFilter;
import com.tfttools.engine.engine_filter.ExcludedTraitFilter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class EngineFilterManager {
    private final ExcludedUnitFilter excludedUnitFilter;
    private final ExcludedTraitFilter excludedTraitFilter;

    public EngineFilterManager(Set<Unit> excludedUnits, Set<Trait> excludedTraits) {
        this.excludedUnitFilter = new ExcludedUnitFilter(excludedUnits);
        this.excludedTraitFilter = new ExcludedTraitFilter(excludedTraits);
    }

    public List<EngineFilter> getEngineFilters() {
        return List.of(excludedUnitFilter, excludedTraitFilter);
    }
}
