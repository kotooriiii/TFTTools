package com.tfttools.service;

import com.tfttools.domain.Champion;
import com.tfttools.domain.Unit;
import com.tfttools.dto.UnitDTO;
import com.tfttools.graph.UnitGraph;
import com.tfttools.mapper.UnitMapper;
import com.tfttools.registry.UnitRegistry;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UnitService
{

    private final UnitRegistry unitRegistry;
    private final UnitMapper unitMapper;
    private final UnitGraph unitGraph;

    public UnitService(UnitRegistry unitRegistry, UnitMapper unitMapper, UnitGraph unitGraph)
    {
        this.unitRegistry = unitRegistry;
        this.unitMapper = unitMapper;
        this.unitGraph = unitGraph;
    }

    public List<UnitDTO> getAllUnits()
    {
        return unitRegistry.getAllUnits().stream().map(unitMapper).collect(Collectors.toList());
    }

    public List<UnitDTO> neighborsOfChampionName(String championName)
    {
        Champion champion = Optional.ofNullable(championName)
                .map(String::toUpperCase)
                .map(name -> {
                    try {
                        return Champion.valueOf(name);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .orElse(null);

        if(null == champion)
        {
            return Collections.emptyList();
        }


        Set<Champion> champions = unitGraph.traverseNeighborsOfChampion(champion);

        List<Unit> units = champions.stream().map(unitRegistry::getUnitByChampion).toList();

        return units.stream().map(unitMapper).toList();
    }
}
