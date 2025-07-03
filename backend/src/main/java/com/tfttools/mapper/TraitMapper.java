package com.tfttools.mapper;

import com.tfttools.domain.Trait;
import com.tfttools.dto.TraitDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Maps internal {@link Trait} object to {@link TraitDTO} object
 */
@Component
public class TraitMapper implements Function<Trait, TraitDTO> {

    @Override
    public TraitDTO apply(Trait trait) {
        return new TraitDTO(trait.getDisplayName());
    }
}
