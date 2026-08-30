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

    private Set<Unit> adaptUnits(List<String> unitApiNames, ValidationContext validation)
    {
        return unitApiNames.stream()
                .map(apiName ->
                {
                    Unit unit = unitRepository.getUnitByApiName(apiName);
                    if (unit == null)
                    {
                        validation.addError("Unknown unit: " + apiName);
                    }
                    return unit;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<Trait> adaptTraits(List<String> traitApiNames, ValidationContext validation)
    {
        return traitApiNames.stream()
                .map(apiName ->
                {
                    Trait trait = traitRepository.getTraitByApiName(apiName);
                    if (trait == null)
                    {
                        validation.addError("Unknown trait: " + apiName);
                    }
                    return trait;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
