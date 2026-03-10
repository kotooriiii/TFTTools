package com.tfttools.engine.enginefilter;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExcludedTraitFilter implements EngineFilter {
    private final Set<Trait> excludedTraits;

    public ExcludedTraitFilter(List<Trait> excludedTraits) {
        this.excludedTraits = new HashSet<>(excludedTraits);
    }

    @Override
    public List<Unit> filter(List<Unit> unitList) {
        for (Unit unit : unitList) {
            Set<Trait> unitTraits = new HashSet<>(unit.getTraits());

            if (unitTraits.contains(excludedTraits)) {
                unitList.remove(unit);
            }
        }

        return unitList;
    }
}
