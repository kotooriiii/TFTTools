package com.tfttools.controller;

import com.tfttools.domain.Champion;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.dto.UnitDTO;
import com.tfttools.service.UnitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * optimization problem -> maximize # of traits with constraints on # units, traits, champions
     * given required traits and champions find most promsising nodes (nodes with most traits / nodes with low trait thresholds) and explore
     * @param numberOfUnits
     * @param numberOfComps
     * @param requiredTraits
     * @param requiredChampions
     * @return
     */
    @GetMapping("/horizontal")
    public List<List<UnitDTO>> getHorizontalComps(int numberOfUnits, int numberOfComps, List<Trait> requiredTraits, List<Integer> thresholds, List<Champion> requiredChampions) {

        return unitService.getHorizontalComps(numberOfUnits, numberOfComps, requiredTraits, thresholds, requiredChampions);
        //dummy value
//        return List.of(List.of(unitService.getAllUnits().get(0)), List.of(unitService.getAllUnits().get(1)));
    }
}
