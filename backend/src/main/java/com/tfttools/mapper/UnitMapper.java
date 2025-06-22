package com.tfttools.mapper;

import com.tfttools.domain.Unit;
import com.tfttools.dto.UnitDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UnitMapper implements Function<Unit, UnitDTO> {

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