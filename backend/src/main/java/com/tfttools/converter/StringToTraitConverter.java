package com.tfttools.converter;

import com.tfttools.domain.Trait;
import com.tfttools.registry.UnitRegistry;
import org.springframework.core.convert.converter.Converter;

public class StringToTraitConverter implements Converter<String, Trait> {
    private final UnitRegistry unitRegistry;

    public StringToTraitConverter(UnitRegistry unitRegistry) {
        this.unitRegistry = unitRegistry;
    }

    @Override
    public Trait convert(String source) {
        return this.unitRegistry.getTraitByName(source);
    }
}
