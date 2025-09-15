package com.tfttools.engine.manager;

import com.tfttools.domain.Champion;
import com.tfttools.domain.Trait;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.registry.UnitRegistry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Manager {
    private final EngineHeuristicManager engineHeuristicManager;
    private final EngineFilterManager engineFilterManager;
    private final EngineTerminatorManager engineTerminatorManager;
    private final EngineStateManager engineStateManager;
    private final UnitRegistry unitRegistry;

    public Manager(HorizontalDTO horizontalDTO, UnitRegistry unitRegistry) {
        List<Champion> requiredChampions = horizontalDTO.getRequiredChampionDTOs().stream().map(championDTO -> Champion.fromDisplayName(championDTO.getDisplayName())).toList();
        Map<Trait, Integer> requiredTraits = horizontalDTO.getRequiredTraitDTOs().entrySet().stream().collect(Collectors.toMap(e -> Trait.fromDisplayName(e.getKey()), Map.Entry::getValue));
        float luck = horizontalDTO.getLuck();
        List<Trait> emblems = horizontalDTO.getEmblems().stream().map(traitDTO -> Trait.fromDisplayName(traitDTO.getDisplayName())).toList();
        int costOfBoard = horizontalDTO.getCostOfBoard();
        int tactitionLevel = horizontalDTO.getTactitionLevel();
        this.unitRegistry = unitRegistry;

        this.engineStateManager = new EngineStateManager(new HashSet<>(), requiredChampions, emblems, tactitionLevel, unitRegistry);

        this.engineHeuristicManager = new EngineHeuristicManager(requiredChampions, requiredTraits, luck, emblems, costOfBoard, engineStateManager.getEngineState());

        List<Champion> excludedChampions = horizontalDTO.getExcludedChampionDTOs().stream().map(championDTO -> Champion.fromDisplayName(championDTO.getDisplayName())).toList();
        List<Trait> excludedTraits = horizontalDTO.getExcludedTraitDTOs().stream().map(traitDTO -> Trait.fromDisplayName(traitDTO.getDisplayName())).toList();
        this.engineFilterManager = new EngineFilterManager(excludedChampions, excludedTraits, requiredChampions, unitRegistry);

        int crowns = horizontalDTO.getCrowns();
        this.engineTerminatorManager = new EngineTerminatorManager(tactitionLevel, crowns);


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
