package com.tfttools.dto;

import java.util.Map;
import java.util.Set;

public record HorizontalDTO(int compSize, Map<String, Integer> requiredTraits, Set<UnitDTO> requiredUnits,
                            Set<TraitDTO> excludedTraits, Set<UnitDTO> excludedUnits, int costOfBoard,
                            int tacticianLevel, int crowns, Set<EmblemDTO> emblems, float luck)
{

}
