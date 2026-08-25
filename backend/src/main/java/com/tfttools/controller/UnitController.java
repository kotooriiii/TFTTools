package com.tfttools.controller;

import com.tfttools.dto.*;
import com.tfttools.service.UnitService;
import com.tfttools.service.SearchService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Manages endpoints for unit requests and search operations
 */
@RestController
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;
    private final SearchService searchService;

    public UnitController(UnitService unitService, SearchService searchService) {
        this.unitService = unitService;
        this.searchService = searchService;
    }

    @GetMapping
    public List<UnitDTO> getAllUnits(@RequestParam(defaultValue = "true") String simple) {
        return unitService.getAllUnits(simple);
    }

    /**
     * General search endpoint that returns all matching entities
     */
    @GetMapping("/search")
    public SearchResultDTO getSuggestions(@RequestParam(defaultValue = "") String query) {
        return unitService.getSuggestions(query);
    }

    /**
     * Champion-specific search endpoint
     */
    @GetMapping("/search/units")
    public List<UnitDTO> searchUnits(@RequestParam String query) {
        return searchService.searchUnits(query);
    }

    /**
     * Trait-specific search endpoint
     */
    @GetMapping("/search/traits")
    public List<TraitDTO> searchTraits(@RequestParam String query) {
        return searchService.searchTraits(query);
    }

    /**
     * Emblem-specific search endpoint
     */
    @GetMapping("/search/emblems")
    public List<EmblemDTO> searchEmblems(@RequestParam String query) {
        return searchService.searchEmblems(query);
    }

    @GetMapping("/filter")
    public List<UnitDTO> filter(FilterDTO filterDTO) {
        return unitService.filter(filterDTO);
    }
}