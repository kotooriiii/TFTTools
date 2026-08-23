package com.tfttools.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class CompositionDTO
{
    private final List<UnitDTO> units;
    private final Map<TraitDTO, Integer> traits;
    private final int activatedTraits;
    private final String teamCode;
    private final List<UnitPlacementDTO> placements;

    public CompositionDTO(List<UnitDTO> units, Map<TraitDTO, Integer> traits, int activatedTraits, String teamCode, List<UnitPlacementDTO> placements)
    {
        this.units = units;
        this.traits = traits;
        this.activatedTraits = activatedTraits;
        this.teamCode = teamCode;
        this.placements = placements;
    }
}
