package com.tfttools.engine.enginefilter;

import com.tfttools.domain.Unit;
import com.tfttools.registry.UnitRegistry;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ExcludedUnitFilter implements EngineFilter{
    private final List<Unit> excludedUnits;
    private final UnitRegistry unitRegistry;

    public ExcludedUnitFilter(List<Unit> excludedUnits, UnitRegistry unitRegistry) {
        this.excludedUnits = excludedUnits;
        this.unitRegistry = unitRegistry;
    }

    @Override
    public List<Unit> filter(List<Unit> unitList) {
        Set<Unit> unitSet = new HashSet<>(unitList);
        unitSet.removeAll(new HashSet<>(excludedUnits));

        return unitSet.stream().toList();
    }
}
