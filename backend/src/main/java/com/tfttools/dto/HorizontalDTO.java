package com.tfttools.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class HorizontalDTO {
    private final int numberOfComps;
    private final Map<String, Integer> requiredTraitDTOs;
    private final List<ChampionDTO> requiredChampionDTOs;
    private final List<TraitDTO> excludedTraitDTOs;
    private final List<ChampionDTO> excludedChampionDTOs;
    private final int costOfBoard;
    private final int tactitionLevel;
    private final int crowns;
    private final List<TraitDTO> emblems;
    private final float luck;


    public HorizontalDTO(
            int numberOfComps,
            Map<String, Integer> requiredTraitDTOs,
            List<ChampionDTO> requiredChampionDTOs,
            List<TraitDTO> excludedTraitDTOs,
            List<ChampionDTO> excludedChampionDTOs,
            int costOfBoard,
            int tactitionLevel,
            int crowns,
            List<TraitDTO> emblems,
            float luck
    ) {
        this.numberOfComps = numberOfComps;
        this.requiredTraitDTOs = requiredTraitDTOs;
        this.requiredChampionDTOs = requiredChampionDTOs;
        this.excludedTraitDTOs = excludedTraitDTOs;
        this.excludedChampionDTOs = excludedChampionDTOs;
        this.costOfBoard = costOfBoard;
        this.tactitionLevel = tactitionLevel;
        this.crowns = crowns;
        this.emblems = emblems;
        this.luck = luck;
    }

    public int getCrowns() {
        return crowns;
    }

    public int getNumberOfComps() {
        return numberOfComps;
    }

    public Map<String, Integer> getRequiredTraitDTOs() {
        return requiredTraitDTOs;
    }

    public List<ChampionDTO> getRequiredChampionDTOs() {
        return requiredChampionDTOs;
    }

    public List<TraitDTO> getExcludedTraitDTOs() {
        return excludedTraitDTOs;
    }

    public List<ChampionDTO> getExcludedChampionDTOs() {
        return excludedChampionDTOs;
    }

    public int getCostOfBoard() {
        return costOfBoard;
    }

    public int getTactitionLevel() {
        return tactitionLevel;
    }

    public List<TraitDTO> getEmblems() {
        return emblems;
    }

    public float getLuck() {
        return luck;
    }
}
