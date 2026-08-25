package com.tfttools.dto;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Consolidates data for transfer from business logic to requestor
 */
@Getter
public class SearchResultDTO {
    private final List<UnitDTO> units;
    private final List<TraitDTO> traits;

    public SearchResultDTO() {
        this.units = Collections.emptyList();
        this.traits = Collections.emptyList();
    }

    public SearchResultDTO(List<UnitDTO> units, List<TraitDTO> traits) {
        this.units = units;
        this.traits = traits;
    }

}
