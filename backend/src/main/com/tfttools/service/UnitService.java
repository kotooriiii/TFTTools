package com.tfttools.service;

import com.tfttools.prefixtrie.PrefixTrieUtils;
import com.tfttools.dto.*;
import com.tfttools.mapper.TraitMapper;
import com.tfttools.mapper.UnitMapper;
import com.tfttools.registry.UnitRegistry;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Business logic for Units
 */
@Service
public class UnitService {

    private final UnitRegistry unitRegistry;
    private final UnitMapper unitMapper;
    private final TraitMapper traitMapper;


    public UnitService(UnitRegistry unitRegistry, UnitMapper unitMapper, TraitMapper traitMapper) {
        this.unitRegistry = unitRegistry;
        this.unitMapper = unitMapper;
        this.traitMapper = traitMapper;
    }

    /**
     * Gets all units from {@link UnitRegistry} and sanitizes it for the requestor
     * @return List of {@link UnitDTO}
     */
    public List<UnitDTO> getAllUnits() {
        return unitRegistry.getAllUnits().stream().map(unitMapper).collect(Collectors.toList());
    }

    public List<TraitDTO> getAllTraits() {
        return unitRegistry.getAllTraits().stream().map(traitMapper).collect(Collectors.toList());
    }

    /**
     * Gets suggestions from {@link UnitRegistry} and sanitizes it for the requestor
     * @param search The input parameter for a search
     * @return SearchResultDTO
     */
    public SearchResultDTO getSuggestions(String search) {
        search = PrefixTrieUtils.removePunctuation(search);
        if (search.isEmpty()) { return new SearchResultDTO(); }

        List<UnitDTO> champs = unitRegistry.getAllChampionsStartingWith(search).stream().map(unitMapper).collect(Collectors.toList());
        List<TraitDTO> traits = unitRegistry.getAllTraitsStartingWith(search).stream().map(traitMapper).collect(Collectors.toList());

        return new SearchResultDTO(champs, traits);
    }

    public List<UnitDTO> filter(FilterDTO filterDTO) {
        return unitRegistry.filter(filterDTO).stream().map(unitMapper).collect(Collectors.toList());
    }

    public List<List<UnitDTO>> getHorizontalComps(HorizontalDTO horizontalDTO) {
        return unitRegistry.getHorizontalComps(horizontalDTO).stream().map(comp -> comp.stream().map(unitMapper).collect(Collectors.toList())).collect(Collectors.toList());
    }
}
