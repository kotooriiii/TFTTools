package com.tfttools.dto;

import java.util.List;

public class FilterDTO {
    private List<ChampionDTO> champions;
    private List<TraitDTO> traits;

    public FilterDTO(List<ChampionDTO> champions, List<TraitDTO> traits) {
        this.champions = champions;
        this.traits = traits;
    }

    public List<ChampionDTO> getChampions() {
        return champions;
    }

    public List<TraitDTO> getTraits() {
        return traits;
    }

    public void setChampions(List<ChampionDTO> champions) {
        this.champions = champions;
    }

    public void setTraits(List<TraitDTO> traits) {
        this.traits = traits;
    }
}
