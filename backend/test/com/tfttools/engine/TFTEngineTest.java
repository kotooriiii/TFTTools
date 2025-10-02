package com.tfttools.engine;
import com.tfttools.domain.Unit;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.dto.UnitDTO;
import com.tfttools.engine.manager.Manager;
import com.tfttools.mapper.TraitMapper;
import com.tfttools.mapper.UnitMapper;
import com.tfttools.registry.UnitRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TFTEngineTest {
    private HorizontalDTO horizontalDTO;
    private UnitRegistry unitRegistry;

    private final TraitMapper traitMapper = new TraitMapper();
    private final UnitMapper unitMapper = new UnitMapper(traitMapper);

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

        List<Set<Unit>> comps = new ArrayList<>();
        List<Unit> unitPool = unitRegistry.getAllUnits();

        Manager manager = new Manager(horizontalDTO, unitRegistry, comps, unitPool);

        TFTEngine tftEngine = new TFTEngine(manager);
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
        List<UnitDTO> requiredUnitDTOs = new ArrayList<>(List.of(unitRegistry.getUnitByName("Kennen"))).stream().map(unitMapper).collect(Collectors.toList());
        List<TraitDTO> excludedTraitDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        List<UnitDTO> excludedUnitDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        int costOfBoard = 999;
        int tactitionLevel = 8;
        int crowns = 0;
        List<TraitDTO> emblems = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        float luck = 0.5f;

        createDTO(numberOfComps, requiredTraitDTOs, requiredUnitDTOs, excludedTraitDTOs, excludedUnitDTOs, costOfBoard, tactitionLevel, crowns, emblems, luck);


        List<Set<Unit>> comps = new ArrayList<>();
        List<Unit> unitPool = unitRegistry.getAllUnits();
        Manager manager = new Manager(horizontalDTO, unitRegistry, comps, unitPool);

        TFTEngine tftEngine = new TFTEngine(manager);
        List<Set<Unit>> tmp = tftEngine.buildComps(numberOfComps);
        for (Set<Unit> comp : tmp) {
            for (Unit unit : comp) {
                System.out.print(unit.getName() + " ");
            }
            System.out.println();
        }

    }

    @Test
    public void testEmblem() {
        int numberOfComps = 3;
        Map<String, Integer> requiredTraitDTOs = new HashMap<>();
        List<UnitDTO> requiredUnitDTOs = new ArrayList<>(List.of());
        List<TraitDTO> excludedTraitDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        List<UnitDTO> excludedUnitDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        int costOfBoard = 999;
        int tactitionLevel = 8;
        int crowns = 0;
        List<TraitDTO> emblems = new ArrayList<>(Stream.of(unitRegistry.getTraitByName("Soul Fighter")).map(traitMapper).toList());//.stream().map(traitMapper).toList();
        float luck = 0.5f;

        createDTO(numberOfComps, requiredTraitDTOs, requiredUnitDTOs, excludedTraitDTOs, excludedUnitDTOs, costOfBoard, tactitionLevel, crowns, emblems, luck);


        List<Set<Unit>> comps = new ArrayList<>();
        List<Unit> unitPool = unitRegistry.getAllUnits();
        Manager manager = new Manager(horizontalDTO, unitRegistry, comps, unitPool);

        TFTEngine tftEngine = new TFTEngine(manager);
        List<Set<Unit>> tmp = tftEngine.buildComps(numberOfComps);
        for (Set<Unit> comp : tmp) {
            for (Unit unit : comp) {
                System.out.print(unit.getName() + " ");
            }
            System.out.println();
        }

    }

    @Test
    public void testLuck() {
        int numberOfComps = 3;
        Map<String, Integer> requiredTraitDTOs = new HashMap<>();
        List<UnitDTO> requiredUnitDTOs = new ArrayList<>(List.of());
        List<TraitDTO> excludedTraitDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        List<UnitDTO> excludedUnitDTOs = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
        int costOfBoard = 999;
        int tactitionLevel = 8;
        int crowns = 0;
        List<TraitDTO> emblems = new ArrayList<>(Stream.of(unitRegistry.getTraitByName("Soul Fighter")).map(traitMapper).toList());//.stream().map(traitMapper).toList();
        float luck = 0.25f;

        createDTO(numberOfComps, requiredTraitDTOs, requiredUnitDTOs, excludedTraitDTOs, excludedUnitDTOs, costOfBoard, tactitionLevel, crowns, emblems, luck);


        List<Set<Unit>> comps = new ArrayList<>();
        List<Unit> unitPool = unitRegistry.getAllUnits();
        Manager manager = new Manager(horizontalDTO, unitRegistry, comps, unitPool);

        TFTEngine tftEngine = new TFTEngine(manager);
        List<Set<Unit>> tmp = tftEngine.buildComps(numberOfComps);
        for (Set<Unit> comp : tmp) {
            for (Unit unit : comp) {
                System.out.print(unit.getName() + " ");
            }
            System.out.println();
        }

        System.out.println();

        luck = 0.75f;

        createDTO(numberOfComps, requiredTraitDTOs, requiredUnitDTOs, excludedTraitDTOs, excludedUnitDTOs, costOfBoard, tactitionLevel, crowns, emblems, luck);


        comps = new ArrayList<>();
        unitPool = unitRegistry.getAllUnits();
        manager = new Manager(horizontalDTO, unitRegistry, comps, unitPool);

        tftEngine = new TFTEngine(manager);
        tmp = tftEngine.buildComps(numberOfComps);
        for (Set<Unit> comp : tmp) {
            for (Unit unit : comp) {
                System.out.print(unit.getName() + " ");
            }
            System.out.println();
        }

    }
}
