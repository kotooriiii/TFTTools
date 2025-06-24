package com.tfttools.mapper;

import com.tfttools.domain.Unit;
import com.tfttools.dto.UnitDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import java.util.function.Function;

/**
 * Maps internal {@link Unit} object to {@link UnitDTO} object
 */
@Component
public class UnitMapper implements Function<Unit, UnitDTO> {

    /**
     * Creates a new {@link UnitDTO} object from internal {@link Unit} object
     * @param unit Unit to be sanitized
     * @return Sanitized Unit object
     */
    @Override
    public UnitDTO apply(Unit unit) {
        return new UnitDTO(
                unit.getChampion().name(),
                unit.getTraits().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet())
        );
    }
}

