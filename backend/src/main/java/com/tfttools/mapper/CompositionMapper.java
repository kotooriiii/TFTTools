
package com.tfttools.mapper;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Trait;
import com.tfttools.dto.CompositionDTO;
import com.tfttools.dto.TraitDTO;
import com.tfttools.dto.UnitDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Maps internal {@link Composition} object to {@link CompositionDTO} object
 */
@Component
public class CompositionMapper implements Function<Composition, CompositionDTO> {

    private final UnitMapper unitMapper;
    private final TraitMapper traitMapper;

    @Autowired
    public CompositionMapper(UnitMapper unitMapper, TraitMapper traitMapper)
    {
        this.unitMapper = unitMapper;
        this.traitMapper = traitMapper;
    }

    @Override
    public CompositionDTO apply(Composition composition) {
        List<UnitDTO> unitDTOs = composition.getUnits().stream()
                .map(unitMapper)
                .collect(Collectors.toList());

        Map<TraitDTO, Integer> traitDTOs = composition.getTraits().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> traitMapper.apply(entry.getKey()),
                        Map.Entry::getValue
                ));


        return new CompositionDTO(unitDTOs, traitDTOs);
    }
}