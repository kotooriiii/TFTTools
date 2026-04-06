package com.tfttools.dto;

import java.util.Map;
import java.util.Set;

public record HorizontalDTO(int compSize, Map<String, Integer> requiredTraits, Set<UnitDTO> requiredChampions,
                            Set<TraitDTO> excludedTraits, Set<UnitDTO> excludedChampions, int costOfBoard,
                            int tactitionLevel, int crowns, Set<EmblemDTO> emblems, float luck)
{

}
