package com.tfttools.adapter;

import com.tfttools.domain.Emblem;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.dto.EmblemDTO;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.dto.UnitDTO;
import com.tfttools.repository.EmblemRepository;
import com.tfttools.repository.TraitRepository;
import com.tfttools.repository.UnitRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class EngineConfigurationAdapter
{
    private final UnitRepository unitRepository;
    private final TraitRepository traitRepository;
    private final EmblemRepository emblemRepository;

    public EngineConfigurationAdapter(UnitRepository unitRepository, TraitRepository traitRepository, EmblemRepository emblemRepository)
    {
        this.unitRepository = unitRepository;
        this.traitRepository = traitRepository;
        this.emblemRepository = emblemRepository;
    }

    public EngineConfiguration adaptToEngineConfiguration(HorizontalDTO input)
    {
        return adaptFromHorizontalDTO(input);
    }

    private EngineConfiguration adaptFromHorizontalDTO(HorizontalDTO horizontalDTO)
    {
        ValidationContext validation = new ValidationContext();

        Set<Unit> requiredUnits = adaptUnits(horizontalDTO.requiredChampions(),
                "required unit", validation);

        Map<Trait, Integer> requiredTraits = adaptRequiredTraits(horizontalDTO.requiredTraits(),
                validation);

        Set<Emblem> emblems = adaptEmblems(horizontalDTO.emblems(), validation);

        Set<Unit> excludedUnits = adaptUnits(horizontalDTO.excludedChampions(),
                "excluded unit", validation);

        Set<Trait> excludedTraits = adaptTraits(horizontalDTO.excludedTraits(),
                validation);

        // Validate business rules
        validateBusinessRules(horizontalDTO, validation);

        // Throw if any validation errors
        validation.throwIfErrors();

        return EngineConfiguration.builder()
                .compSize(horizontalDTO.compSize())
                .requiredTraits(requiredTraits)
                .requiredUnits(requiredUnits)
                .excludedTraits(excludedTraits)
                .excludedUnits(excludedUnits)
                .costOfBoard(horizontalDTO.costOfBoard())
                .tactitionLevel(horizontalDTO.tactitionLevel())
                .crowns(horizontalDTO.crowns())
                .emblems(emblems)
                .luck(horizontalDTO.luck())
                .build();
    }

    private Set<Unit> adaptUnits(Set<UnitDTO> unitDTOs, String context, ValidationContext validation)
    {
        return unitDTOs.stream()
                .map(unitDTO ->
                {
                    Unit unit = unitRepository.getUnitByName(unitDTO.getDisplayName());
                    if (unit == null)
                    {
                        validation.addError("Unknown " + context + ": " + unitDTO.getDisplayName());
                    }
                    return unit;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Map<Trait, Integer> adaptRequiredTraits(Map<String, Integer> requiredTraitsDTO,
                                                    ValidationContext validation)
    {
        Map<Trait, Integer> result = new HashMap<>();

        for (Map.Entry<String, Integer> entry : requiredTraitsDTO.entrySet())
        {
            Trait trait = traitRepository.getTraitByName(entry.getKey());
            if (trait == null)
            {
                validation.addError("Unknown required trait: " + entry.getKey());
                continue;
            }

            if (entry.getValue() <= 0)
            {
                validation.addError("Required trait count must be positive for: " + entry.getKey());
                continue;
            }
            result.put(trait, entry.getValue());

        }
        return result;
    }
        


private Set<Emblem> adaptEmblems(Set<EmblemDTO> emblemDTOs, ValidationContext validation)
{
    return emblemDTOs.stream()
            .map(emblemDTO ->
            {
                Emblem emblem = emblemRepository.getEmblemByName(emblemDTO.getDisplayName());
                if (emblem == null)
                {
                    validation.addError("Unknown emblem trait: " + emblemDTO.getDisplayName());
                }
                return emblem;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
}

private Set<Trait> adaptTraits(Set<TraitDTO> traitDTOs, ValidationContext validation)
{
    return traitDTOs.stream()
            .map(traitDTO ->
            {
                Trait trait = traitRepository.getTraitByName(traitDTO.getDisplayName());
                if (trait == null)
                {
                    validation.addError("Unknown excluded trait: " + traitDTO.getDisplayName());
                }
                return trait;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
}

private void validateBusinessRules(HorizontalDTO horizontalDTO, ValidationContext validation)
{
    // Validate comp size
    if (horizontalDTO.compSize() <= 0 || horizontalDTO.compSize() > 10)
    {
        validation.addError("Invalid comp size: " + horizontalDTO.compSize());
    }

    // Validate tactition level
    if (horizontalDTO.tactitionLevel() < 1 || horizontalDTO.tactitionLevel() > 10)
    {
        validation.addError("Invalid tactition level: " + horizontalDTO.tactitionLevel());
    }

    // Validate luck
    if (horizontalDTO.luck() < 0.0f || horizontalDTO.luck() > 1.0f)
    {
        validation.addError("Luck must be between 0.0 and 1.0");
    }

    // Validate crowns
    if (horizontalDTO.crowns() < 0)
    {
        validation.addError("Crowns cannot be negative");
    }

    // Validate cost of board
    if (horizontalDTO.costOfBoard() < 0)
    {
        validation.addError("Cost of board cannot be negative");
    }
}
    

}
