package com.tfttools.service;

import com.tfttools.PrefixTrie.PrefixTrieUtils;
import com.tfttools.dto.*;
import com.tfttools.mapper.ChampionMapper;
import com.tfttools.mapper.TraitMapper;
import com.tfttools.mapper.UnitMapper;
import com.tfttools.registry.UnitRegistry;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for Units
 */
@Service
public class UnitService {

    private final UnitRegistry unitRegistry;
    private final UnitMapper unitMapper;
    private final ChampionMapper championMapper;
    private final TraitMapper traitMapper;


    public UnitService(UnitRegistry unitRegistry, UnitMapper unitMapper, ChampionMapper championMapper, TraitMapper traitMapper) {
        this.unitRegistry = unitRegistry;
        this.unitMapper = unitMapper;
        this.championMapper = championMapper;
        this.traitMapper = traitMapper;
    }

    /**
     * Gets all units from {@link UnitRegistry} and sanitizes it for the requestor
     * @return List of {@link UnitDTO}
     */
    public List<UnitDTO> getAllUnits() {
        return unitRegistry.getAllUnits().stream().map(unitMapper).collect(Collectors.toList());
    }

    /**
     * Gets suggestions from {@link UnitRegistry} and sanitizes it for the requestor
     * @param search The input parameter for a search
     * @return SearchResultDTO
     */
    public SearchResultDTO getSuggestions(String search) {
        search = PrefixTrieUtils.removePunctuation(search);
        if (search.isEmpty()) { return new SearchResultDTO(); }

        List<ChampionDTO> champs = unitRegistry.getAllChampionsStartingWith(search).stream().map(championMapper).collect(Collectors.toList());
        List<TraitDTO> traits = unitRegistry.getAllTraitsStartingWith(search).stream().map(traitMapper).collect(Collectors.toList());

        return new SearchResultDTO(champs, traits);
    }

    public List<UnitDTO> getUnitsOf(FilterDTO filterDTO) {
        return unitRegistry.getUnitsOf(filterDTO).stream().map(unitMapper).collect(Collectors.toList());
    }
}
