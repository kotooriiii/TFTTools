
package com.tfttools.mapper;

import com.tfttools.domain.Composition;
import com.tfttools.domain.UnitPlacement;
import com.tfttools.dto.CompositionDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.dto.UnitDTO;
import com.tfttools.dto.UnitPlacementDTO;
import com.tfttools.service.CompositionPositioningService;
import com.tfttools.service.TeamPlannerService;
import com.tfttools.util.CompositionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Maps internal {@link Composition} object to {@link CompositionDTO} object
 */
@Component
public class CompositionMapper implements Function<Composition, CompositionDTO> {

    private final UnitMapperSimple unitMapperSimple;
    private final TraitMapper traitMapper;
    private final TeamPlannerService teamPlannerService;
    private final CompositionPositioningService compositionPositioningService;

    @Autowired
    public CompositionMapper(UnitMapperSimple unitMapperSimple, TraitMapper traitMapper, TeamPlannerService teamPlannerService, CompositionPositioningService compositionPositioningService) {
        this.unitMapperSimple = unitMapperSimple;
        this.traitMapper = traitMapper;
        this.teamPlannerService = teamPlannerService;
        this.compositionPositioningService = compositionPositioningService;
    }


    @Override
    public CompositionDTO apply(Composition composition) {
        List<UnitDTO> unitDTOs = composition.getUnits().stream()
                .map(unitMapperSimple)
                .collect(Collectors.toList());

        Map<TraitDTO, Integer> traitDTOs = composition.getTraits().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> traitMapper.apply(entry.getKey()),
                        Map.Entry::getValue
                ));

        final int activatedTraits = CompositionUtils.INSTANCE.getActivatedTraits(composition).size();
        final String teamCode = teamPlannerService.exportToTeamCode(composition);

        List<UnitPlacementDTO> placements = compositionPositioningService.place(composition).stream()
                .map(placement -> new UnitPlacementDTO(unitMapperSimple.apply(placement.getUnit()), placement.getRow(), placement.getCol()))
                .collect(Collectors.toList());

        return new CompositionDTO(unitDTOs, traitDTOs, activatedTraits, teamCode, placements);
    }
}