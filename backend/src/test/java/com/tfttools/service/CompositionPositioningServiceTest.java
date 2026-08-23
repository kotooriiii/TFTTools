package com.tfttools.service;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Role;
import com.tfttools.domain.Unit;
import com.tfttools.domain.UnitPlacement;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CompositionPositioningServiceTest {

    private final CompositionPositioningService service = new CompositionPositioningService();

    private static Unit unit(String name, int cost, Role role) {
        return new Unit(name, name, cost, role, null, List.of());
    }

    private static List<String> toKeys(List<UnitPlacement> placements) {
        return placements.stream()
                .map(p -> p.getUnit().getDisplayName() + ":" + p.getRow() + "-" + p.getCol())
                .toList();
    }

    @Test
    void singleTankLandsAtFrontCenterAnchor() {
        Composition composition = new Composition(List.of(unit("Tank1", 3, Role.APTANK)));

        List<UnitPlacement> placements = service.place(composition);

        assertThat(placements).hasSize(1);
        assertThat(placements.get(0).getRow()).isEqualTo(0);
        assertThat(placements.get(0).getCol()).isEqualTo(3);
    }

    @Test
    void twoTanksBothOnFrontRowWithNoCollision() {
        Composition composition = new Composition(List.of(
                unit("TankA", 3, Role.APTANK),
                unit("TankB", 4, Role.ADTANK)
        ));

        List<UnitPlacement> placements = service.place(composition);

        assertThat(placements).hasSize(2);
        assertThat(placements).allMatch(p -> p.getRow() == 0);
        Set<Integer> cols = placements.stream().map(UnitPlacement::getCol).collect(Collectors.toSet());
        assertThat(cols).hasSize(2);
    }

    @Test
    void twoCarriesLandInOppositeBackCorners() {
        Composition composition = new Composition(List.of(
                unit("CarryA", 4, Role.ADCARRY),
                unit("CarryB", 5, Role.APCARRY)
        ));

        List<UnitPlacement> placements = service.place(composition);

        assertThat(placements).hasSize(2);
        assertThat(placements).allMatch(p -> p.getRow() == 3);
        Set<Integer> cols = placements.stream().map(UnitPlacement::getCol).collect(Collectors.toSet());
        assertThat(cols).containsExactlyInAnyOrder(0, 6);
    }

    @Test
    void carryGetsIdealCornerCasterGetsNextNearest() {
        Composition composition = new Composition(List.of(
                unit("Carry1", 5, Role.APCARRY),
                unit("Caster1", 4, Role.APCASTER)
        ));

        List<UnitPlacement> placements = service.place(composition);

        UnitPlacement carryPlacement = placements.stream()
                .filter(p -> p.getUnit().getDisplayName().equals("Carry1"))
                .findFirst().orElseThrow();
        UnitPlacement casterPlacement = placements.stream()
                .filter(p -> p.getUnit().getDisplayName().equals("Caster1"))
                .findFirst().orElseThrow();

        assertThat(carryPlacement.getRow()).isEqualTo(3);
        assertThat(carryPlacement.getCol()).isEqualTo(0);

        assertThat(casterPlacement.getRow() + "-" + casterPlacement.getCol())
                .isNotEqualTo(carryPlacement.getRow() + "-" + carryPlacement.getCol());
        assertThat(casterPlacement.getRow()).isBetween(0, 3);
        assertThat(casterPlacement.getCol()).isBetween(0, 6);
    }

    @Test
    void tenUnitCompositionProducesNoDuplicatesAndStaysInBounds() {
        List<Unit> units = List.of(
                unit("Tank1", 3, Role.APTANK),
                unit("Tank2", 2, Role.ADTANK),
                unit("Fighter1", 3, Role.APFIGHTER),
                unit("Fighter2", 4, Role.ADFIGHTER),
                unit("Carry1", 5, Role.APCARRY),
                unit("Carry2", 4, Role.ADCARRY),
                unit("Caster1", 3, Role.APCASTER),
                unit("Caster2", 2, Role.ADCASTER),
                unit("Specialist1", 1, Role.APSPECIALIST),
                unit("Specialist2", 2, Role.HYBRIDSPECIALIST)
        );
        Composition composition = new Composition(units);

        List<UnitPlacement> placements = service.place(composition);

        assertThat(placements).hasSize(10);
        Set<String> hexKeys = placements.stream()
                .map(p -> p.getRow() + "-" + p.getCol())
                .collect(Collectors.toSet());
        assertThat(hexKeys).hasSize(10);
        assertThat(placements).allSatisfy(p -> {
            assertThat(p.getRow()).isBetween(0, 3);
            assertThat(p.getCol()).isBetween(0, 6);
        });
    }

    @Test
    void hybridTankPositionsLikeBaseTank() {
        Composition apComposition = new Composition(List.of(unit("T", 3, Role.APTANK)));
        Composition hybridComposition = new Composition(List.of(unit("T", 3, Role.HYBRIDTANK)));

        UnitPlacement apPlacement = service.place(apComposition).get(0);
        UnitPlacement hybridPlacement = service.place(hybridComposition).get(0);

        assertThat(hybridPlacement.getRow()).isEqualTo(apPlacement.getRow());
        assertThat(hybridPlacement.getCol()).isEqualTo(apPlacement.getCol());
    }

    @Test
    void placementIsDeterministicAcrossRepeatedCalls() {
        Composition composition = new Composition(List.of(
                unit("A", 3, Role.APTANK),
                unit("B", 4, Role.ADCARRY),
                unit("C", 2, Role.APCASTER)
        ));

        List<UnitPlacement> first = service.place(composition);
        List<UnitPlacement> second = service.place(composition);

        assertThat(toKeys(first)).isEqualTo(toKeys(second));
    }

    @Test
    void emptyCompositionProducesNoPlacements() {
        Composition composition = new Composition();

        List<UnitPlacement> placements = service.place(composition);

        assertThat(placements).isEmpty();
    }
}
