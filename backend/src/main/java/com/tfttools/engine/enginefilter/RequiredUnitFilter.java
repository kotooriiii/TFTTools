package com.tfttools.engine.enginefilter;

import com.tfttools.domain.Unit;
import com.tfttools.registry.UnitRegistry;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RequiredUnitFilter implements EngineFilter{
    private final List<Unit> requiredUnit;
    private final UnitRegistry unitRegistry;

    public RequiredUnitFilter(List<Unit> requiredUnit, UnitRegistry unitRegistry) {
        this.requiredUnit = requiredUnit;
        this.unitRegistry = unitRegistry;
    }

    @Override
    public List<Unit> filter(List<Unit> unitList) {
        Set<Unit> unitSet = new HashSet<>(unitList);
        unitSet.removeAll(new HashSet<>(requiredUnit));

        return unitSet.stream().toList();
    }

    public Set<Unit> getRequiredUnits() {
        return new HashSet<>(requiredUnit);
    }
}
