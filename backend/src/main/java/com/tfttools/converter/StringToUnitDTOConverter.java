package com.tfttools.converter;

import com.tfttools.dto.UnitDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUnitDTOConverter implements Converter<String, UnitDTO> {
    @Override
    public UnitDTO convert(String source) {
        return new UnitDTO(source);
    }
}
