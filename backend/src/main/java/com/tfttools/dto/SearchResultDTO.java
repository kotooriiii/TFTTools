package com.tfttools.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Consolidates data for transfer from business logic to requestor
 */
public class SearchResultDTO {
    private final List<ChampionDTO> championList;
    private final List<TraitDTO> traitList;

    public SearchResultDTO() {
        this.championList = Collections.emptyList();
        this.traitList = Collections.emptyList();
    }

    public SearchResultDTO(List<ChampionDTO> championList, List<TraitDTO> traitList) {
        this.championList = championList;
        this.traitList = traitList;
    }

    public List<ChampionDTO> getChampionList() {
        return championList;
    }

    public List<TraitDTO> getTraitList() {
        return traitList;
    }
}
