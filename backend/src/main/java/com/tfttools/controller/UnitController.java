package com.tfttools.controller;

import com.tfttools.dto.UnitDTO;
import com.tfttools.service.UnitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Manages endpoints for unit requests
 */
@RestController
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;

    /**
     * Constructor, autoinjects UnitService object for use in
     * the class
     * @param unitService UnitService object which handles business logic
     */
    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    /**
     * Uses unitService to get all units from unit registry
     * @return Returns a list of UnitDTO objects for all units in the unit registry
     */
    @GetMapping
    public List<UnitDTO> getAllUnits() {
        return unitService.getAllUnits();
    }
}
