package com.tfttools.engine.manager;

import com.tfttools.domain.Champion;
import com.tfttools.domain.Trait;
import com.tfttools.engine.enginefilter.EngineFilter;
import com.tfttools.engine.enginefilter.ExcludedChampionFilter;
import com.tfttools.engine.enginefilter.ExcludedTraitFilter;
import com.tfttools.engine.enginefilter.RequiredChampionFilter;
import com.tfttools.registry.UnitRegistry;

import java.util.List;

public class EngineFilterManager {
    ExcludedChampionFilter excludedChampionFilter;
    ExcludedTraitFilter excludedTraitFilter;
    RequiredChampionFilter requiredChampionFilter;
    UnitRegistry unitRegistry;

    public EngineFilterManager(List<Champion> excludedChampions, List<Trait> excludedTraits, List<Champion> requiredChampions, UnitRegistry unitRegistry) {
        this.excludedChampionFilter = new ExcludedChampionFilter(excludedChampions, unitRegistry);
        this.excludedTraitFilter = new ExcludedTraitFilter(excludedTraits);
        this.requiredChampionFilter = new RequiredChampionFilter(requiredChampions, unitRegistry);
    }

    public List<EngineFilter> getEngineFilters() {
        return List.of(excludedChampionFilter, excludedTraitFilter, requiredChampionFilter);
    }
}
