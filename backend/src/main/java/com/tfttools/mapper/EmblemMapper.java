package com.tfttools.mapper;

import com.tfttools.domain.Emblem;
import com.tfttools.domain.Trait;
import com.tfttools.dto.EmblemDTO;
import com.tfttools.dto.TraitDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Maps internal {@link Trait} object to {@link TraitDTO} object
 */
@Component
public class EmblemMapper implements Function<Emblem, EmblemDTO> {

    @Override
    public EmblemDTO apply(Emblem emblem) {
        return new EmblemDTO(emblem.getDisplayName());
    }
}
