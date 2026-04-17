package com.tfttools.service;

import com.tfttools.domain.Trait;
import com.tfttools.dto.EmblemDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.dto.UnitDTO;
import com.tfttools.mapper.EmblemMapper;
import com.tfttools.mapper.TraitMapper;
import com.tfttools.mapper.UnitMapper;
import com.tfttools.repository.EmblemRepository;
import com.tfttools.repository.TraitRepository;
import com.tfttools.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService
{

    private final UnitRepository unitRepository;
    private final TraitRepository traitRepository;
    private final EmblemRepository emblemRepository;

    private final UnitMapper unitMapper;
    private final TraitMapper traitMapper;
    private final EmblemMapper emblemMapper;

    public SearchService(UnitRepository unitRepository, TraitRepository traitRepository, EmblemRepository emblemRepository,
                         UnitMapper unitMapper, TraitMapper traitMapper, EmblemMapper emblemMapper)
    {
        this.unitRepository = unitRepository;
        this.traitRepository = traitRepository;
        this.emblemRepository = emblemRepository;
        this.unitMapper = unitMapper;
        this.traitMapper = traitMapper;
        this.emblemMapper = emblemMapper;
    }


    public List<UnitDTO> searchChampions(String query)
    {
        return unitRepository.getAllChampionsStartingWith(query)
                .stream()
                .map(unitMapper)
                .collect(Collectors.toList());
    }

    public List<TraitDTO> searchTraits(String query)
    {
        return traitRepository.getAllTraitsStartingWith(query)
                .stream()
                .filter(Trait::isCountable)
                .map(traitMapper)
                .collect(Collectors.toList());
    }

    public List<EmblemDTO> searchEmblems(String query)
    {
        return emblemRepository.getAllEmblemsStartingWith(query)
                .stream()
                .map(emblemMapper)
                .collect(Collectors.toList());
    }
}
