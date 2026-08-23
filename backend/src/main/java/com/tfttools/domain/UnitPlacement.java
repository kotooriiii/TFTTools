package com.tfttools.domain;

import lombok.Getter;

/**
 * Pairs a {@link Unit} with its assigned hex board position for display purposes.
 */
@Getter
public class UnitPlacement {

    private final Unit unit;
    private final int row;
    private final int col;

    public UnitPlacement(Unit unit, int row, int col) {
        this.unit = unit;
        this.row = row;
        this.col = col;
    }
}
