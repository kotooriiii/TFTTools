package com.tfttools.controller;

import com.tfttools.dto.ChampionDTO;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.dto.UnitDTO;
import com.tfttools.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tools")
public class ToolsController
{

    private final UnitService unitService;

    public ToolsController(UnitService unitService) {
        this.unitService = unitService;
    }

    /**
     * Finds comps with the most possible active traits given the # of units per comp, # of comps to generate, required traits + thresholds,
     * and required champions
     *
     * @param horizontalDTO
     */
    @PostMapping("/horizontal")
    public List<List<UnitDTO>> getHorizontalComps(@RequestBody HorizontalDTO horizontalDTO) {
        return unitService.getHorizontalComps(horizontalDTO);
    }
}
