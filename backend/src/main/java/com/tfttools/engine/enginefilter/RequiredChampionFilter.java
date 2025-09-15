package com.tfttools.engine.enginefilter;

import com.tfttools.domain.Champion;
import com.tfttools.domain.Unit;
import com.tfttools.registry.UnitRegistry;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RequiredChampionFilter implements EngineFilter{
    private final List<Champion> requiredChampions;
    private final UnitRegistry unitRegistry;

    public RequiredChampionFilter(List<Champion> requiredChampions, UnitRegistry unitRegistry) {
        this.requiredChampions = requiredChampions;
        this.unitRegistry = unitRegistry;
    }

    @Override
    public List<Unit> filter(List<Unit> unitList) {
        Set<Unit> unitSet = new HashSet<>(unitList);
        unitSet.removeAll(requiredChampions.stream().map(unitRegistry::getUnitByChampion).collect(Collectors.toSet()));

        return unitSet.stream().toList();
    }

    public Set<Unit> getRequiredUnits() {
        return requiredChampions.stream().map(unitRegistry::getUnitByChampion).collect(Collectors.toSet());
    }
}
