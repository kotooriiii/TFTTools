package com.tfttools.dto;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Consolidates data for transfer from business logic to requestor
 */
@Getter
public class SearchResultDTO {
    private final List<UnitDTO> championDTO;
    private final List<TraitDTO> traitList;

    public SearchResultDTO() {
        this.championDTO = Collections.emptyList();
        this.traitList = Collections.emptyList();
    }

    public SearchResultDTO(List<UnitDTO> championList, List<TraitDTO> traitList) {
        this.championDTO = championList;
        this.traitList = traitList;
    }

}
