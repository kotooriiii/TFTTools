package com.tfttools.mapper;

import com.tfttools.domain.Unit;
import com.tfttools.dto.UnitDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Maps internal {@link Unit} object to {@link UnitDTO} object
 */
@Component
public class UnitMapper implements Function<Unit, UnitDTO> {
    private final TraitMapper traitMapper;

    public UnitMapper(TraitMapper traitMapper) {
        this.traitMapper = traitMapper;
    }

    /**
     * Creates a new {@link UnitDTO} object from internal {@link Unit} object
     * @param unit Unit to be sanitized
     * @return Sanitized Unit object
     */
    @Override
    public UnitDTO apply(Unit unit) {
        return new UnitDTO(
                unit.getDisplayName(),
                unit.getTraits().stream().map(traitMapper).collect(Collectors.toSet())
        );
    }
}