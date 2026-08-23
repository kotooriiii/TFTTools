package com.tfttools.dto;

import lombok.Getter;

/**
 * Sanitized unit + hex board position for display purposes.
 */
@Getter
public class UnitPlacementDTO {

    private final UnitDTO unit;
    private final int row;
    private final int col;

    public UnitPlacementDTO(UnitDTO unit, int row, int col) {
        this.unit = unit;
        this.row = row;
        this.col = col;
    }
}
