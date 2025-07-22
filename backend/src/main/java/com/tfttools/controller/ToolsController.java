package com.tfttools.controller;

import com.tfttools.dto.ChampionDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.dto.UnitDTO;
import com.tfttools.service.UnitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * @param numberOfUnits Number of units to include per composition
     * @param numberOfComps Target number of compositions to generate
     * @param requiredTraitDTOs Required traits that each composition must contain
     * @param thresholds The minimum thresholds for each required trait
     * @param requiredChampionDTOs Required champions that each composition must contain
     * @return List of generated compositions
     */
    @GetMapping("/horizontal")
    public List<List<UnitDTO>> getHorizontalComps(@RequestParam int numberOfUnits, @RequestParam int numberOfComps, @RequestParam List<TraitDTO> requiredTraitDTOs, @RequestParam List<Integer> thresholds, @RequestParam List<ChampionDTO> requiredChampionDTOs) {
        return unitService.getHorizontalComps(numberOfUnits, numberOfComps, requiredTraitDTOs, thresholds, requiredChampionDTOs);
    }
}
