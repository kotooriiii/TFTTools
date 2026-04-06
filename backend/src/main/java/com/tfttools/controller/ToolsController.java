package com.tfttools.controller;

import com.tfttools.dto.CompositionDTO;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.dto.UnitDTO;
import com.tfttools.service.CompositionService;
import com.tfttools.service.UnitService;
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
     * and required champions
     *
     * @param horizontalDTO
     */
    @PostMapping("/horizontal")
    public List<CompositionDTO> getHorizontalComps(@RequestBody HorizontalDTO horizontalDTO) {
        return compositionService.generateCompositions(horizontalDTO);
    }
}
