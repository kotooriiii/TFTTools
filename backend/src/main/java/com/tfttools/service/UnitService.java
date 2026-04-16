package com.tfttools.service;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.dto.FilterDTO;
import com.tfttools.dto.SearchResultDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.dto.UnitDTO;
import com.tfttools.mapper.TraitMapper;
import com.tfttools.mapper.UnitMapper;
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

    private final UnitMapper unitMapper;
    private final TraitMapper traitMapper;

    public UnitService(UnitRepository unitRepository, TraitRepository traitRepository,
                       UnitMapper unitMapper, TraitMapper traitMapper)
    {
        this.unitRepository = unitRepository;
        this.traitRepository = traitRepository;
        this.unitMapper = unitMapper;
        this.traitMapper = traitMapper;
    }

    /**
     * Gets all units from {@link UnitRepository} and sanitizes it for the requestor
     *
     * @return List of {@link UnitDTO}
     */
    public List<UnitDTO> getAllUnits()
    {
        return unitRepository.getAllUnits().stream().map(unitMapper).collect(Collectors.toList());
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

        List<UnitDTO> champs = unitRepository.getAllChampionsStartingWith(search).stream().map(unitMapper).collect(Collectors.toList());
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
        Set<Unit> filteredUnits = new HashSet<>(unitRepository.getAllUnits());

        if (!filterDTO.getUnits().isEmpty())
        {
            // From unitDTOList map displayName -> actual unit
            List<Unit> unitList = filterDTO.getUnits().stream().map(unitDTO -> unitRepository.getUnitByName(unitDTO.getDisplayName())).toList();

            // For each unit in unitList map unit -> SingletonSet(Unit)
            List<Set<Unit>> unitsFromChampions = unitList.stream().map(Collections::singleton).toList();

            // For each unit take intersection of filteredUnits and UnitSingleton
            unitsFromChampions.forEach(filteredUnits::retainAll);

        }

        if (!filterDTO.getTraits().isEmpty())
        {
            // From traitList map displayName -> Trait
            List<Trait> traitList = filterDTO.getTraits().stream().map(traitDTO -> traitRepository.getTraitByName(traitDTO.getDisplayName())).toList();
            // For each trait, take intersection of filteredUnits and Set(all units in trait)
            traitList.forEach(trait -> filteredUnits.retainAll(new HashSet<>(unitRepository.getUnitsByTrait(trait))));
        }

        return filteredUnits.stream().map(unitMapper).collect(Collectors.toList());
    }


}
