package com.tfttools.converter;

import com.tfttools.domain.Unit;
import com.tfttools.repository.UnitRepository;
import org.springframework.core.convert.converter.Converter;

public class StringToUnitConverter implements Converter<String, Unit> {
    private final UnitRepository unitRepository;

    public StringToUnitConverter(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    public Unit convert(String source) {
        return this.unitRepository.getUnitByName(source);
    }
}
