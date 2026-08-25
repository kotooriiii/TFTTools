package com.tfttools.service;

import com.tfttools.adapter.UnitFilterAdapter;
import com.tfttools.adapter.UnitFilterCriteria;
import com.tfttools.domain.Unit;
import com.tfttools.dto.FilterDTO;
import com.tfttools.dto.SearchResultDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.dto.UnitDTO;
import com.tfttools.mapper.TraitMapper;
import com.tfttools.mapper.UnitMapperDetailed;
import com.tfttools.mapper.UnitMapperSimple;
import com.tfttools.prefixtrie.PrefixTrieUtils;
import com.tfttools.repository.TraitRepository;
import com.tfttools.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Business logic for Units
 */
@Service
public class UnitService
{
    private final UnitRepository unitRepository;
    private final TraitRepository traitRepository;
    private final UnitFilterAdapter unitFilterAdapter;

    private final UnitMapperSimple unitMapperSimple;
    private final UnitMapperDetailed unitMapperDetailed;
    private final TraitMapper traitMapper;

    public UnitService(UnitRepository unitRepository, TraitRepository traitRepository, UnitFilterAdapter unitFilterAdapter,
                       UnitMapperSimple unitMapperSimple, UnitMapperDetailed unitMapperDetailed, TraitMapper traitMapper)
    {
        this.unitRepository = unitRepository;
        this.traitRepository = traitRepository;
        this.unitFilterAdapter = unitFilterAdapter;
        this.unitMapperSimple = unitMapperSimple;
        this.unitMapperDetailed = unitMapperDetailed;
        this.traitMapper = traitMapper;
    }

    /**
     * Gets all units from {@link UnitRepository} and sanitizes it for the requestor
     *
     * @return List of {@link UnitDTO}
     */
    public List<UnitDTO> getAllUnits(String simple)
    {
        if (PrefixTrieUtils.removePunctuation(simple).equalsIgnoreCase("true")) {
            return unitRepository.getAllUnits().stream().map(unitMapperSimple).collect(Collectors.toList());
        } else {
            return unitRepository.getAllUnits().stream().map(unitMapperDetailed).collect(Collectors.toList());
        }
    }

    public List<TraitDTO> getAllTraits()
    {
        return traitRepository.getAllTraits().stream().map(traitMapper).collect(Collectors.toList());
    }

    /**
     * Gets suggestions from {@link UnitRepository} and {@link TraitRepository} and sanitizes it for the requestor
     *
     * @param search The input parameter for a search
     * @return SearchResultDTO
     */
    public SearchResultDTO getSuggestions(String search)
    {
        search = PrefixTrieUtils.removePunctuation(search);
        if (search.isEmpty())
        {
            return new SearchResultDTO();
        }

        List<UnitDTO> champs = unitRepository.getAllChampionsStartingWith(search).stream().map(unitMapperSimple).collect(Collectors.toList());
        List<TraitDTO> traits = traitRepository.getAllTraitsStartingWith(search).stream().map(traitMapper).collect(Collectors.toList());

        return new SearchResultDTO(champs, traits);
    }

    /**
     * Gets units according to filter params in FilterDTO
     *
     * @param filterDTO Contains filter data {@link FilterDTO}
     * @return List of units {@link Unit}
     */
    public List<UnitDTO> filter(FilterDTO filterDTO)
    {
        if (filterDTO == null || (filterDTO.getUnits().isEmpty() && filterDTO.getTraits().isEmpty()))
        {
            return Collections.emptyList();
        }

        // Adapter handles validation and conversion
        UnitFilterCriteria criteria = unitFilterAdapter.adaptToFilterCriteria(filterDTO);

        // Union of explicitly selected units and units belonging to any selected trait
        Set<Unit> filteredUnits = new HashSet<>(criteria.units());
        criteria.traits().forEach(trait -> filteredUnits.addAll(unitRepository.getUnitsByTrait(trait)));

        return filteredUnits.stream().map(unitMapperSimple).collect(Collectors.toList());
    }


}
