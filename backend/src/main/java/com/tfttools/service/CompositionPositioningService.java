package com.tfttools.service;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Role;
import com.tfttools.domain.Unit;
import com.tfttools.domain.UnitPlacement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Assigns each unit in a {@link Composition} a hex coordinate on the same 7-col x 4-row offset
 * grid rendered by the frontend Comp Builder board (frontend/src/components/CompBuilder/hexUtils.ts
 * - HEX_COLS/HEX_ROWS must stay in sync with that file), so composition results can be shown on a
 * board without the user re-placing units by hand. Row 0 is the front.
 */
@Service
public class CompositionPositioningService {

    private static final int HEX_COLS = 7;
    private static final int HEX_ROWS = 4;

    private enum Zone {
        TANK, FIGHTER, CARRY, CASTER, SPECIALIST
    }

    // Assignment priority: tanks claim the front first, specialists are placed last.
    private static final List<Zone> ZONE_PRIORITY = List.of(Zone.TANK, Zone.FIGHTER, Zone.CARRY, Zone.CASTER, Zone.SPECIALIST);

    private record Hex(int row, int col) {
    }

    private static final List<Hex> ALL_HEXES = buildAllHexes();

    private static final Hex TANK_ANCHOR = new Hex(0, 3);
    private static final Hex FIGHTER_ANCHOR = new Hex(1, 3);
    private static final Hex SPECIALIST_ANCHOR = new Hex(3, 3);
    // Both back corners - Carry gets first pick (processed before Caster), Caster inherits whichever is left.
    private static final List<Hex> CARRY_CASTER_ANCHORS = List.of(new Hex(3, 0), new Hex(3, 6));

    private static List<Hex> buildAllHexes() {
        List<Hex> hexes = new ArrayList<>();
        for (int row = 0; row < HEX_ROWS; row++) {
            for (int col = 0; col < HEX_COLS; col++) {
                hexes.add(new Hex(row, col));
            }
        }
        return hexes;
    }

    public List<UnitPlacement> place(Composition composition) {
        Set<Hex> occupied = new HashSet<>();
        List<UnitPlacement> placements = new ArrayList<>();

        for (Zone zone : ZONE_PRIORITY) {
            List<Unit> zoneUnits = composition.getUnits().stream()
                    .filter(unit -> classify(unit.getRole()) == zone)
                    .sorted(Comparator.comparingInt(Unit::getCost).reversed()
                            .thenComparing(Unit::getDisplayName))
                    .toList();

            for (int i = 0; i < zoneUnits.size(); i++) {
                Unit unit = zoneUnits.get(i);
                Hex anchor = anchorFor(zone, i);
                Hex chosen = nearestUnoccupied(anchor, occupied);
                occupied.add(chosen);
                placements.add(new UnitPlacement(unit, chosen.row(), chosen.col()));
            }
        }

        return placements;
    }

    private static Hex anchorFor(Zone zone, int indexInZone) {
        return switch (zone) {
            case TANK -> TANK_ANCHOR;
            case FIGHTER -> FIGHTER_ANCHOR;
            case SPECIALIST -> SPECIALIST_ANCHOR;
            case CARRY, CASTER -> CARRY_CASTER_ANCHORS.get(indexInZone % CARRY_CASTER_ANCHORS.size());
        };
    }

    private static Hex nearestUnoccupied(Hex anchor, Set<Hex> occupied) {
        return ALL_HEXES.stream()
                .filter(hex -> !occupied.contains(hex))
                .min(Comparator.comparingInt((Hex hex) -> hexDistance(anchor, hex))
                        .thenComparingInt(Hex::row)
                        .thenComparingInt(Hex::col))
                .orElseThrow(() -> new IllegalStateException("No unoccupied hex available on the board"));
    }

    private static int hexDistance(Hex a, Hex b) {
        int[] cubeA = toCube(a);
        int[] cubeB = toCube(b);
        return (Math.abs(cubeA[0] - cubeB[0]) + Math.abs(cubeA[1] - cubeB[1]) + Math.abs(cubeA[2] - cubeB[2])) / 2;
    }

    // Offset (row, col) -> cube coordinates, "odd-r" layout (odd rows shifted right),
    // matching hexUtils.ts's getHexCenter row % 2 === 1 offset.
    private static int[] toCube(Hex hex) {
        int x = hex.col() - (hex.row() - (hex.row() & 1)) / 2;
        int z = hex.row();
        int y = -x - z;
        return new int[]{x, y, z};
    }

    private static Zone classify(Role role) {
        if (role == null) {
            return Zone.FIGHTER;
        }

        String name = role.name(); // e.g. APTANK, HYBRIDCARRY
        if (name.endsWith("TANK")) return Zone.TANK;
        if (name.endsWith("FIGHTER")) return Zone.FIGHTER;
        if (name.endsWith("CARRY")) return Zone.CARRY;
        if (name.endsWith("CASTER")) return Zone.CASTER;
        if (name.endsWith("SPECIALIST")) return Zone.SPECIALIST;
        return Zone.FIGHTER;
    }
}
