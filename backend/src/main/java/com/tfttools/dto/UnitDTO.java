package com.tfttools.dto;

import java.util.Set;

/**
 * Sanitize unit object for data transfer from business logic to requestor
 */
public class UnitDTO {
    private String unit;
    private Set<TraitDTO> traits;

    public UnitDTO(String unit, Set<TraitDTO> traits) {
        this.unit = unit;
        this.traits = traits;
    }

    public String getUnit() {
        return unit;
    }

    public Set<TraitDTO> getTraits() {
        return traits;
    }
}
