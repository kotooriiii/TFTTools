package com.tfttools.engine;
import com.tfttools.domain.Unit;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.dto.UnitDTO;
import com.tfttools.mapper.TraitMapper;
import com.tfttools.mapper.UnitMapper;
import com.tfttools.registry.UnitRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TFTEngineTest {
//    private HorizontalDTO horizontalDTO;
//    private UnitRegistry unitRegistry;
//
//    private final TraitMapper traitMapper = new TraitMapper();
//    private final UnitMapper unitMapper = new UnitMapper(traitMapper);
//
//    @BeforeEach
//    public void initTests() {
//        this.unitRegistry = new UnitRegistry();
//    }
//
//    public void createDTO(int compSize, Map<String, Integer> requiredTraits, List<UnitDTO> requiredChampions, List<TraitDTO> excludedTraits, List<UnitDTO> excludedChampions, int costOfBoard, int tactitionLevel, int crowns, List<TraitDTO> emblems, float luck) {
//        horizontalDTO = new HorizontalDTO(compSize, requiredTraits, requiredChampions, excludedTraits, excludedChampions, costOfBoard, tactitionLevel, crowns, emblems, luck);
//    }
//
//    @Test
//    public void testBasic() {
//        int compSize = 3;
//        Map<String, Integer> requiredTraits = new HashMap<>();
//        List<UnitDTO> requiredChampions = new ArrayList<>(List.of());//.stream().map(champion -> championMapper.apply(champion)).collect(Collectors.toList());
//        List<TraitDTO> excludedTraits = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
//        List<UnitDTO> excludedChampions = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
//        int costOfBoard = 999;
//        int tactitionLevel = 8;
//        int crowns = 0;
//        List<TraitDTO> emblems = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
//        float luck = 0.5f;
//
//        createDTO(compSize, requiredTraits, requiredChampions, excludedTraits, excludedChampions, costOfBoard, tactitionLevel, crowns, emblems, luck);
//
//        List<Set<Unit>> comps = new ArrayList<>();
//        List<Unit> unitPool = unitRegistry.getAllUnits();
//
//        TFTEngineManager TFTEngineManager = new TFTEngineManager(horizontalDTO, unitRegistry, comps, unitPool);
//
//        TFTEngine tftEngine = new TFTEngine(TFTEngineManager);
//        List<Set<Unit>> tmp = tftEngine.buildComps(compSize);
//        for (Set<Unit> comp : tmp) {
//            for (Unit unit : comp) {
//                System.out.print(unit.getName() + " ");
//            }
//            System.out.println();
//        }
//
//    }
//
//    @Test
//    public void testRequiredUnits() {
//        int compSize = 3;
//        Map<String, Integer> requiredTraits = new HashMap<>();
//        List<UnitDTO> requiredChampions = new ArrayList<>(List.of(unitRegistry.getUnitByName("Kennen"))).stream().map(unitMapper).collect(Collectors.toList());
//        List<TraitDTO> excludedTraits = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
//        List<UnitDTO> excludedChampions = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
//        int costOfBoard = 999;
//        int tactitionLevel = 8;
//        int crowns = 0;
//        List<TraitDTO> emblems = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
//        float luck = 0.5f;
//
//        createDTO(compSize, requiredTraits, requiredChampions, excludedTraits, excludedChampions, costOfBoard, tactitionLevel, crowns, emblems, luck);
//
//
//        List<Set<Unit>> comps = new ArrayList<>();
//        List<Unit> unitPool = unitRegistry.getAllUnits();
//        TFTEngineManager TFTEngineManager = new TFTEngineManager(horizontalDTO, unitRegistry, comps, unitPool);
//
//        TFTEngine tftEngine = new TFTEngine(TFTEngineManager);
//        List<Set<Unit>> tmp = tftEngine.buildComps(compSize);
//        for (Set<Unit> comp : tmp) {
//            for (Unit unit : comp) {
//                System.out.print(unit.getName() + " ");
//            }
//            System.out.println();
//        }
//
//    }
//
//    @Test
//    public void testEmblem() {
//        int compSize = 3;
//        Map<String, Integer> requiredTraits = new HashMap<>();
//        List<UnitDTO> requiredChampions = new ArrayList<>(List.of());
//        List<TraitDTO> excludedTraits = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
//        List<UnitDTO> excludedChampions = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
//        int costOfBoard = 999;
//        int tactitionLevel = 8;
//        int crowns = 0;
//        List<TraitDTO> emblems = new ArrayList<>(Stream.of(unitRegistry.getTraitByName("Soul Fighter")).map(traitMapper).toList());//.stream().map(traitMapper).toList();
//        float luck = 0.5f;
//
//        createDTO(compSize, requiredTraits, requiredChampions, excludedTraits, excludedChampions, costOfBoard, tactitionLevel, crowns, emblems, luck);
//
//
//        List<Set<Unit>> comps = new ArrayList<>();
//        List<Unit> unitPool = unitRegistry.getAllUnits();
//        TFTEngineManager TFTEngineManager = new TFTEngineManager(horizontalDTO, unitRegistry, comps, unitPool);
//
//        TFTEngine tftEngine = new TFTEngine(TFTEngineManager);
//        List<Set<Unit>> tmp = tftEngine.buildComps(compSize);
//        for (Set<Unit> comp : tmp) {
//            for (Unit unit : comp) {
//                System.out.print(unit.getName() + " ");
//            }
//            System.out.println();
//        }
//
//    }
//
//    @Test
//    public void testLuck() {
//        int compSize = 3;
//        Map<String, Integer> requiredTraits = new HashMap<>();
//        List<UnitDTO> requiredChampions = new ArrayList<>(List.of());
//        List<TraitDTO> excludedTraits = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
//        List<UnitDTO> excludedChampions = new ArrayList<>(List.of());//.stream().map(traitMapper).toList();
//        int costOfBoard = 999;
//        int tactitionLevel = 8;
//        int crowns = 0;
//        List<TraitDTO> emblems = new ArrayList<>(Stream.of(unitRegistry.getTraitByName("Soul Fighter")).map(traitMapper).toList());//.stream().map(traitMapper).toList();
//        float luck = 0.25f;
//
//        createDTO(compSize, requiredTraits, requiredChampions, excludedTraits, excludedChampions, costOfBoard, tactitionLevel, crowns, emblems, luck);
//
//
//        List<Set<Unit>> comps = new ArrayList<>();
//        List<Unit> unitPool = unitRegistry.getAllUnits();
//        TFTEngineManager TFTEngineManager = new TFTEngineManager(horizontalDTO, unitRegistry, comps, unitPool);
//
//        TFTEngine tftEngine = new TFTEngine(TFTEngineManager);
//        List<Set<Unit>> tmp = tftEngine.buildComps(compSize);
//        for (Set<Unit> comp : tmp) {
//            for (Unit unit : comp) {
//                System.out.print(unit.getName() + " ");
//            }
//            System.out.println();
//        }
//
//        System.out.println();
//
//        luck = 0.75f;
//
//        createDTO(compSize, requiredTraits, requiredChampions, excludedTraits, excludedChampions, costOfBoard, tactitionLevel, crowns, emblems, luck);
//
//
//        comps = new ArrayList<>();
//        unitPool = unitRegistry.getAllUnits();
//        TFTEngineManager = new TFTEngineManager(horizontalDTO, unitRegistry, comps, unitPool);
//
//        tftEngine = new TFTEngine(TFTEngineManager);
//        tmp = tftEngine.buildComps(compSize);
//        for (Set<Unit> comp : tmp) {
//            for (Unit unit : comp) {
//                System.out.print(unit.getName() + " ");
//            }
//            System.out.println();
//        }
//
//    }
}
