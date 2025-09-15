package com.tfttools.engine;
import com.tfttools.domain.Champion;
import com.tfttools.domain.Unit;
import com.tfttools.dto.ChampionDTO;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.engine.manager.EngineFilterManager;
import com.tfttools.engine.manager.EngineHeuristicManager;
import com.tfttools.engine.manager.EngineTerminatorManager;
import com.tfttools.engine.manager.Manager;
import com.tfttools.mapper.ChampionMapper;
import com.tfttools.mapper.TraitMapper;
import com.tfttools.registry.UnitRegistry;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class TFTEngineTest {
    HorizontalDTO horizontalDTO;
    UnitRegistry unitRegistry;
    ChampionMapper championMapper = new ChampionMapper();
    TraitMapper traitMapper = new TraitMapper();

    public void createDTO(int numberOfComps, Map<TraitDTO, Integer> requiredTraitDTOs, List<ChampionDTO> requiredChampionDTOs, List<TraitDTO> excludedTraitDTOs, List<ChampionDTO> excludedChampionDTOs, int costOfBoard, int tactitionLevel, int crowns, List<TraitDTO> emblems, float luck) {
        horizontalDTO = new HorizontalDTO(numberOfComps, requiredTraitDTOs, requiredChampionDTOs, excludedTraitDTOs, excludedChampionDTOs, costOfBoard, tactitionLevel, crowns, emblems, luck);
        unitRegistry = new UnitRegistry();
    }

    @Test
    public void testBasic() {
        int numberOfComps = 3;
        Map<TraitDTO, Integer> requiredTraitDTOs = new HashMap<>();
        List<ChampionDTO> requiredChampionDTOs = new ArrayList<>(List.of());//.stream().map(champion -> championMapper.apply(champion)).collect(Collectors.toList());
        List<TraitDTO> excludedTraitDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        List<ChampionDTO> excludedChampionDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        int costOfBoard = 999;
        int tactitionLevel = 8;
        int crowns = 0;
        List<TraitDTO> emblems = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        float luck = 0.5f;

        createDTO(numberOfComps, requiredTraitDTOs, requiredChampionDTOs, excludedTraitDTOs, excludedChampionDTOs, costOfBoard, tactitionLevel, crowns, emblems, luck);

        Manager manager = new Manager(horizontalDTO, unitRegistry);

        EngineHeuristicManager engineHeuristicManager = manager.getEngineHeuristicManager();
        EngineTerminatorManager engineTerminatorManager = manager.getEngineTerminatorManager();
        EngineFilterManager engineFilterManager = manager.getEngineFilterManager();
        EngineState engineState = manager.getEngineStateManager().getEngineState();
        List<Set<Unit>> comps = new ArrayList<>();
        List<Unit> unitPool = unitRegistry.getAllUnits();

        TFTEngine tftEngine = new TFTEngine(engineHeuristicManager, engineTerminatorManager, engineFilterManager, engineState, comps, unitPool);
        List<Set<Unit>> tmp = tftEngine.buildComps(numberOfComps);
        for (Set<Unit> comp : tmp) {
            for (Unit unit : comp) {
                System.out.print(unit.getChampion().getDisplayName() + " ");
            }
            System.out.println();
        }

    }

    @Test
    public void testRequiredChampions() {
        int numberOfComps = 3;
        Map<TraitDTO, Integer> requiredTraitDTOs = new HashMap<>();
        List<ChampionDTO> requiredChampionDTOs = new ArrayList<>(List.of(Champion.KENNEN)).stream().map(champion -> championMapper.apply(champion)).collect(Collectors.toList());
        List<TraitDTO> excludedTraitDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        List<ChampionDTO> excludedChampionDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        int costOfBoard = 999;
        int tactitionLevel = 8;
        int crowns = 0;
        List<TraitDTO> emblems = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        float luck = 0.5f;

        createDTO(numberOfComps, requiredTraitDTOs, requiredChampionDTOs, excludedTraitDTOs, excludedChampionDTOs, costOfBoard, tactitionLevel, crowns, emblems, luck);

        Manager manager = new Manager(horizontalDTO, unitRegistry);

        EngineHeuristicManager engineHeuristicManager = manager.getEngineHeuristicManager();
        EngineTerminatorManager engineTerminatorManager = manager.getEngineTerminatorManager();
        EngineFilterManager engineFilterManager = manager.getEngineFilterManager();
        EngineState engineState = manager.getEngineStateManager().getEngineState();
        List<Set<Unit>> comps = new ArrayList<>();
        List<Unit> unitPool = unitRegistry.getAllUnits();

        TFTEngine tftEngine = new TFTEngine(engineHeuristicManager, engineTerminatorManager, engineFilterManager, engineState, comps, unitPool);
        List<Set<Unit>> tmp = tftEngine.buildComps(numberOfComps);
        for (Set<Unit> comp : tmp) {
            for (Unit unit : comp) {
                System.out.print(unit.getChampion().getDisplayName() + " ");
            }
            System.out.println();
        }

    }
}
