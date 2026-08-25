package com.tfttools.adapter;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.dto.FilterDTO;
import com.tfttools.repository.TraitRepository;
import com.tfttools.repository.UnitRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UnitFilterAdapter
{
    private final UnitRepository unitRepository;
    private final TraitRepository traitRepository;

    public UnitFilterAdapter(UnitRepository unitRepository, TraitRepository traitRepository)
    {
        this.unitRepository = unitRepository;
        this.traitRepository = traitRepository;
    }

    public UnitFilterCriteria adaptToFilterCriteria(FilterDTO filterDTO)
    {
        ValidationContext validation = new ValidationContext();

        Set<Unit> units = adaptUnits(filterDTO.getUnits(), validation);
        Set<Trait> traits = adaptTraits(filterDTO.getTraits(), validation);

        validation.throwIfErrors();

        return new UnitFilterCriteria(units, traits);
    }

    private Set<Unit> adaptUnits(List<String> unitNames, ValidationContext validation)
    {
        return unitNames.stream()
                .map(name ->
                {
                    Unit unit = unitRepository.getUnitByName(name);
                    if (unit == null)
                    {
                        validation.addError("Unknown unit: " + name);
                    }
                    return unit;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<Trait> adaptTraits(List<String> traitNames, ValidationContext validation)
    {
        return traitNames.stream()
                .map(name ->
                {
                    Trait trait = traitRepository.getTraitByName(name);
                    if (trait == null)
                    {
                        validation.addError("Unknown trait: " + name);
                    }
                    return trait;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
