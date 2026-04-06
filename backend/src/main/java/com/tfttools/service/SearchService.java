package com.tfttools.service;

import com.tfttools.domain.Trait;
import com.tfttools.dto.EmblemDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.dto.UnitDTO;
import com.tfttools.mapper.EmblemMapper;
import com.tfttools.mapper.TraitMapper;
import com.tfttools.mapper.UnitMapper;
import com.tfttools.registry.UnitRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService
{

    private final UnitRegistry unitRegistry;
    private final UnitMapper unitMapper;
    private final TraitMapper traitMapper;
    private final EmblemMapper emblemMapper;

    public SearchService(UnitRegistry unitRegistry, UnitMapper unitMapper,
                         TraitMapper traitMapper, EmblemMapper emblemMapper)
    {
        this.unitRegistry = unitRegistry;
        this.unitMapper = unitMapper;
        this.traitMapper = traitMapper;
        this.emblemMapper = emblemMapper;
    }


    public List<UnitDTO> searchChampions(String query)
    {
        return unitRegistry.getAllChampionsStartingWith(query)
                .stream()
                .map(unitMapper)
                .collect(Collectors.toList());
    }

    public List<TraitDTO> searchTraits(String query)
    {
        return unitRegistry.getAllTraitsStartingWith(query)
                .stream()
                .filter(Trait::isCountable)
                .map(traitMapper)
                .collect(Collectors.toList());
    }

    public List<EmblemDTO> searchEmblems(String query)
    {
        return unitRegistry.getAllEmblemsStartingWith(query)
                .stream()
                .map(emblemMapper)
                .collect(Collectors.toList());
    }
}
