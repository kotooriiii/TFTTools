package com.tfttools.dto;

import lombok.Getter;

import java.util.Set;

/**
 * Sanitize unit object for data transfer from business logic to requestor
 */
@Getter
public class UnitDTO {
    private final String displayName;
    private final Set<TraitDTO> traits;

    public UnitDTO(String displayName, Set<TraitDTO> traits) {
        this.displayName = displayName;
        this.traits = traits;
    }

    public UnitDTO(String displayName) {
        this(displayName, null);
    }

}
