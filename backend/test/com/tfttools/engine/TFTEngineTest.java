package com.tfttools.engine;
import com.tfttools.domain.Unit;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.dto.UnitDTO;
import com.tfttools.engine.manager.EngineFilterManager;
import com.tfttools.engine.manager.EngineHeuristicManager;
import com.tfttools.engine.manager.EngineTerminatorManager;
import com.tfttools.engine.manager.Manager;
import com.tfttools.mapper.TraitMapper;
import com.tfttools.mapper.UnitMapper;
import com.tfttools.registry.UnitRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class TFTEngineTest {
    HorizontalDTO horizontalDTO;
    UnitRegistry unitRegistry;

    TraitMapper traitMapper = new TraitMapper();
    UnitMapper unitMapper = new UnitMapper(traitMapper);

    @BeforeEach
    public void initTests() {
        this.unitRegistry = new UnitRegistry();
    }

    public void createDTO(int numberOfComps, Map<String, Integer> requiredTraitDTOs, List<UnitDTO> requiredUnitDTOs, List<TraitDTO> excludedTraitDTOs, List<UnitDTO> excludedUnitDTOs, int costOfBoard, int tactitionLevel, int crowns, List<TraitDTO> emblems, float luck) {
        horizontalDTO = new HorizontalDTO(numberOfComps, requiredTraitDTOs, requiredUnitDTOs, excludedTraitDTOs, excludedUnitDTOs, costOfBoard, tactitionLevel, crowns, emblems, luck);
    }

    @Test
    public void testBasic() {
        int numberOfComps = 3;
        Map<String, Integer> requiredTraitDTOs = new HashMap<>();
        List<UnitDTO> requiredUnitDTOs = new ArrayList<>(List.of());//.stream().map(champion -> championMapper.apply(champion)).collect(Collectors.toList());
        List<TraitDTO> excludedTraitDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        List<UnitDTO> excludedUnitDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        int costOfBoard = 999;
        int tactitionLevel = 8;
        int crowns = 0;
        List<TraitDTO> emblems = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        float luck = 0.5f;

        createDTO(numberOfComps, requiredTraitDTOs, requiredUnitDTOs, excludedTraitDTOs, excludedUnitDTOs, costOfBoard, tactitionLevel, crowns, emblems, luck);

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
                System.out.print(unit.getName() + " ");
            }
            System.out.println();
        }

    }

    @Test
    public void testRequiredUnits() {
        int numberOfComps = 3;
        Map<String, Integer> requiredTraitDTOs = new HashMap<>();
        List<UnitDTO> requiredUnitDTOs = new ArrayList<>(List.of(unitRegistry.getUnitByName("Kennen"))).stream().map(unit -> unitMapper.apply(unit)).collect(Collectors.toList());
        List<TraitDTO> excludedTraitDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        List<UnitDTO> excludedUnitDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        int costOfBoard = 999;
        int tactitionLevel = 8;
        int crowns = 0;
        List<TraitDTO> emblems = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        float luck = 0.5f;

        createDTO(numberOfComps, requiredTraitDTOs, requiredUnitDTOs, excludedTraitDTOs, excludedUnitDTOs, costOfBoard, tactitionLevel, crowns, emblems, luck);

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
                System.out.print(unit.getName() + " ");
            }
            System.out.println();
        }

    }
}
