package com.tfttools.service;

import com.tfttools.domain.Champion;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.prefixtrie.PrefixTrieUtils;
import com.tfttools.dto.*;
import com.tfttools.mapper.ChampionMapper;
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

    public List<UnitDTO> filter(FilterDTO filterDTO) {
        return unitRegistry.filter(filterDTO).stream().map(unitMapper).collect(Collectors.toList());
    }

    public List<List<UnitDTO>> getHorizontalComps(int numberOfUnits, int numberOfComps, List<Trait> requiredTraits, List<Integer> thresholds, List<Champion> requiredChampions) {
        Set<Unit> start = new HashSet<>(requiredChampions.stream().map(unitRegistry::getUnitByChampion).toList());

        // start by creating a comp with the required traits
        for (int i=0; i<requiredTraits.size(); i++) {
            // get all units from the trait
            List<Unit> units = unitRegistry.getUnitsByTrait(requiredTraits.get(i));

            // meet trait threshold
            Collections.shuffle(units);
            start.addAll(units.subList(0, thresholds.get(i)));
        }

        // now we have a set of starting units that meet the trait thresholds
        return new ArrayList<>();
    }
}
