package com.tfttools.engine.manager;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.registry.UnitRegistry;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Manager {
    private final EngineHeuristicManager engineHeuristicManager;
    private final EngineFilterManager engineFilterManager;
    private final EngineTerminatorManager engineTerminatorManager;
    private final EngineStateManager engineStateManager;
    private final UnitRegistry unitRegistry;
    private final List<Set<Unit>> comps;
    private final List<Unit> unitPool;

    public Manager(HorizontalDTO horizontalDTO, UnitRegistry unitRegistry, List<Set<Unit>> comps, List<Unit> unitPool) {
        List<Unit> requiredUnits = horizontalDTO.getRequiredUnitDTOs().stream().map(unitDTO -> unitRegistry.getUnitByName(unitDTO.getUnit())).toList();
        Map<Trait, Integer> requiredTraits = horizontalDTO.getRequiredTraitDTOs().entrySet().stream().collect(Collectors.toMap(e -> unitRegistry.getTraitByName(e.getKey()), Map.Entry::getValue));
        float luck = horizontalDTO.getLuck();
        List<Trait> emblems = horizontalDTO.getEmblems().stream().map(traitDTO -> unitRegistry.getTraitByName(traitDTO.getDisplayName())).toList();
        int costOfBoard = horizontalDTO.getCostOfBoard();
        int tactitionLevel = horizontalDTO.getTactitionLevel();
        this.unitRegistry = unitRegistry;
        this.comps = comps;
        this.unitPool = unitPool;

        this.engineStateManager = new EngineStateManager(new HashSet<>(), requiredUnits, emblems, tactitionLevel, unitRegistry);

        this.engineHeuristicManager = new EngineHeuristicManager(requiredUnits, requiredTraits, luck, emblems, costOfBoard, engineStateManager.getEngineState());

        List<Unit> excludedUnits = horizontalDTO.getExcludedUnitDTOs().stream().map(unitDTO -> unitRegistry.getUnitByName(unitDTO.getUnit())).toList();
        List<Trait> excludedTraits = horizontalDTO.getExcludedTraitDTOs().stream().map(traitDTO -> unitRegistry.getTraitByName(traitDTO.getDisplayName())).toList();
        this.engineFilterManager = new EngineFilterManager(excludedUnits, excludedTraits, requiredUnits, unitRegistry);

        int crowns = horizontalDTO.getCrowns();
        this.engineTerminatorManager = new EngineTerminatorManager(tactitionLevel, crowns);


    }

    public List<Set<Unit>> getComps() {
        return comps;
    }

    public List<Unit> getUnitPool() {
        return unitPool;
    }

    public EngineHeuristicManager getEngineHeuristicManager() {
        return engineHeuristicManager;
    }

    public EngineFilterManager getEngineFilterManager() {
        return engineFilterManager;
    }

    public EngineTerminatorManager getEngineTerminatorManager() {
        return engineTerminatorManager;
    }

    public EngineStateManager getEngineStateManager() {
        return engineStateManager;
    }
}
