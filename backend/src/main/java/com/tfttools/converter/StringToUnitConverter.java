package com.tfttools.converter;

import com.tfttools.domain.Unit;
import com.tfttools.domain.repository.UnitRepository;
import com.tfttools.dto.UnitDTO;
import com.tfttools.registry.UnitRegistry;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class StringToUnitConverter implements Converter<String, Unit> {
    private final UnitRegistry unitRegistry;

    public StringToUnitConverter(UnitRegistry unitRegistry) {
        this.unitRegistry = unitRegistry;
    }

    @Override
    public Unit convert(String source) {
        return this.unitRegistry.getUnitByName(source);
    }
}
