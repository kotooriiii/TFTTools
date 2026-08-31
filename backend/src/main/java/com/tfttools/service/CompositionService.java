package com.tfttools.service;

import com.tfttools.adapter.EngineConfigurationAdapter;
import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Unit;
import com.tfttools.dto.CompositionDTO;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.engine.TFTEngine;
import com.tfttools.mapper.CompositionMapper;
import com.tfttools.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompositionService {

    private final EngineConfigurationAdapter adapter;
    private final CompositionMapper compositionMapper;
    private final UnitRepository unitRepository;
    private final TeamPlannerService teamPlannerService;

    public CompositionService(EngineConfigurationAdapter adapter, CompositionMapper compositionMapper, UnitRepository unitRepository, TeamPlannerService teamPlannerService)
    {
        this.adapter = adapter;
        this.compositionMapper = compositionMapper;
        this.unitRepository = unitRepository;
        this.teamPlannerService = teamPlannerService;
    }

    public List<CompositionDTO> generateCompositions(HorizontalDTO horizontalDTO) {
        // Adapter handles validation and conversion
        EngineConfiguration config = adapter.adaptToEngineConfiguration(horizontalDTO);

        TFTEngine engine = new TFTEngine(config, unitRepository);

        return engine.buildCompositions().stream().map(compositionMapper).toList();
    }

    /**
     * Resolves an ordered list of unit apiNames to a team code, without needing a full
     * engine-generated {@link Composition} - backs both the My Comps expanded view and Team
     * Builder saves, neither of which has one.
     */
    public String generateTeamCode(List<String> unitApiNames) {
        List<Unit> units = new ArrayList<>();
        List<String> unknownApiNames = new ArrayList<>();

        for (String apiName : unitApiNames) {
            Unit unit = unitRepository.getUnitByApiName(apiName);
            if (unit == null) {
                unknownApiNames.add(apiName);
            } else {
                units.add(unit);
            }
        }

        if (!unknownApiNames.isEmpty()) {
            throw new IllegalArgumentException("Unknown unit apiName(s): " + String.join(", ", unknownApiNames));
        }

        return teamPlannerService.exportToTeamCode(units);
    }
}