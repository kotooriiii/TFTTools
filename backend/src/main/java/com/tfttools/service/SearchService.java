package com.tfttools.service;

import com.tfttools.dto.ChampionDTO;
import com.tfttools.dto.EmblemDTO;
import com.tfttools.dto.SearchResultDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.mapper.ChampionMapper;
import com.tfttools.mapper.EmblemMapper;
import com.tfttools.mapper.TraitMapper;
import com.tfttools.registry.UnitRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    
    private final UnitRegistry unitRegistry;
    private final ChampionMapper championMapper;
    private final TraitMapper traitMapper;
    private final EmblemMapper emblemMapper;

    public SearchService(UnitRegistry unitRegistry, ChampionMapper championMapper, 
                        TraitMapper traitMapper, EmblemMapper emblemMapper) {
        this.unitRegistry = unitRegistry;
        this.championMapper = championMapper;
        this.traitMapper = traitMapper;
        this.emblemMapper = emblemMapper;
    }


    public List<ChampionDTO> searchChampions(String query) {
        return unitRegistry.getAllChampionsStartingWith(query)
            .stream()
            .map(championMapper)
            .collect(Collectors.toList());
    }

    public List<TraitDTO> searchTraits(String query) {
        return unitRegistry.getAllTraitsStartingWith(query)
            .stream()
            .map(traitMapper)
            .collect(Collectors.toList());
    }

    public List<EmblemDTO> searchEmblems(String query) {
        return unitRegistry.getAllEmblemsStartingWith(query)
            .stream()
            .map(emblemMapper)
            .collect(Collectors.toList());
    }
}
