package com.tfttools.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class CompositionDTO
{
    private final List<UnitDTO> units;
    private final Map<TraitDTO, Integer> traits;

    public CompositionDTO(List<UnitDTO> units, Map<TraitDTO, Integer> traits)
    {
        this.units = units;
        this.traits = traits;
    }
}
