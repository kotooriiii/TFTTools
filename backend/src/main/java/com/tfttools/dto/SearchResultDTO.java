package com.tfttools.dto;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Consolidates data for transfer from business logic to requestor
 */
@Getter
public class SearchResultDTO {
    private final List<UnitDTO> unitDTOList;
    private final List<TraitDTO> traitList;

    public SearchResultDTO() {
        this.unitDTOList = Collections.emptyList();
        this.traitList = Collections.emptyList();
    }

    public SearchResultDTO(List<UnitDTO> championList, List<TraitDTO> traitList) {
        this.unitDTOList = championList;
        this.traitList = traitList;
    }

}
