package com.tfttools.converter;

import com.tfttools.domain.Trait;
import com.tfttools.dto.TraitDTO;
import org.springframework.core.convert.converter.Converter;

public class StringToTraitDTOConverter implements Converter<String, TraitDTO> {
    @Override
    public TraitDTO convert(String source) {
        return new TraitDTO(source, Trait.fromDisplayName(source).getThresholds());
    }
}
