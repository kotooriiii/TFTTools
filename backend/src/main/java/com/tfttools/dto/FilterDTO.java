package com.tfttools.dto;

import java.util.List;

public class FilterDTO {
    private List<ChampionDTO> championDTOList;
    private List<TraitDTO> traitDTOList;

    public FilterDTO(List<ChampionDTO> championDTOList, List<TraitDTO> traitDTOList) {
        this.championDTOList = championDTOList;
        this.traitDTOList = traitDTOList;
    }

    public List<ChampionDTO> getChampionDTOList() {
        return championDTOList;
    }

    public List<TraitDTO> getTraitDTOList() {
        return traitDTOList;
    }

    public void setChampionDTOList(List<ChampionDTO> championDTOList) {
        this.championDTOList = championDTOList;
    }

    public void setTraitDTOList(List<TraitDTO> traitDTOList) {
        this.traitDTOList = traitDTOList;
    }
}
