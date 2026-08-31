package com.tfttools.comps.dto;

import com.tfttools.comps.domain.Placement;
import jakarta.validation.constraints.NotBlank;

public record PlacementDTO(
        @NotBlank String unitApiName,
        int row,
        int col
) {
    public static PlacementDTO from(Placement placement)
    {
        return new PlacementDTO(placement.unitApiName(), placement.row(), placement.col());
    }

    public Placement toPlacement()
    {
        return new Placement(unitApiName, row, col);
    }
}
