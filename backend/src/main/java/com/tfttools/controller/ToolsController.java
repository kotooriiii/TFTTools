package com.tfttools.controller;

import com.tfttools.dto.CompositionDTO;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.dto.TeamCodeRequest;
import com.tfttools.dto.TeamCodeResponse;
import com.tfttools.dto.UnitDTO;
import com.tfttools.service.CompositionService;
import com.tfttools.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tools")
public class ToolsController
{

    private final CompositionService compositionService;

    public ToolsController(CompositionService compositionService) {
        this.compositionService = compositionService;
    }

    /**
     * Finds comps with the most possible active traits given the # of units per comp, # of comps to generate, required traits + thresholds,
     * and required units
     *
     * @param horizontalDTO
     */
    @PostMapping("/horizontal")
    public List<CompositionDTO> getHorizontalComps(@RequestBody HorizontalDTO horizontalDTO) {
        return compositionService.generateCompositions(horizontalDTO);
    }

    /**
     * Takes an ordered list of unit apiNames and returns a team code, reusing
     * {@link com.tfttools.service.TeamPlannerService}'s hex-code logic without requiring a full
     * engine-generated {@link com.tfttools.domain.Composition}. Backs both the My Comps expanded
     * view and Team Builder saves.
     */
    @PostMapping("/team-code")
    public TeamCodeResponse getTeamCode(@Valid @RequestBody TeamCodeRequest request) {
        return new TeamCodeResponse(compositionService.generateTeamCode(request.unitApiNames()));
    }
}
