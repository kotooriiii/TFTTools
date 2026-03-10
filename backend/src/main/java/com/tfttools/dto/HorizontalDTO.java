package com.tfttools.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class HorizontalDTO {
    private final int numberOfComps;
    private final Map<String, Integer> requiredTraitDTOs;
    private final List<UnitDTO> requiredUnitDTOs;
    private final List<TraitDTO> excludedTraitDTOs;
    private final List<UnitDTO> excludedUnitDTOs;
    private final int costOfBoard;
    private final int tactitionLevel;
    private final int crowns;
    private final List<TraitDTO> emblems;
    private final float luck;


    public HorizontalDTO(
            int numberOfComps,
            Map<String, Integer> requiredTraitDTOs,
            List<UnitDTO> requiredUnitDTOs,
            List<TraitDTO> excludedTraitDTOs,
            List<UnitDTO> excludedUnitDTOs,
            int costOfBoard,
            int tactitionLevel,
            int crowns,
            List<TraitDTO> emblems,
            float luck
    ) {
        this.numberOfComps = numberOfComps;
        this.requiredTraitDTOs = requiredTraitDTOs;
        this.requiredUnitDTOs = requiredUnitDTOs;
        this.excludedTraitDTOs = excludedTraitDTOs;
        this.excludedUnitDTOs = excludedUnitDTOs;
        this.costOfBoard = costOfBoard;
        this.tactitionLevel = tactitionLevel;
        this.crowns = crowns;
        this.emblems = emblems;
        this.luck = luck;
    }

}
