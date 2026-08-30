package com.tfttools.mapper;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.dto.TraitDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Maps internal {@link Trait} object to {@link TraitDTO} object.
 */
@Component
public class TraitMapper implements Function<Trait, TraitDTO> {

    /**
     * Generic mapping with no specific owning unit; count defaults to 1.
     */
    @Override
    public TraitDTO apply(Trait trait) {
        return new TraitDTO(trait.getDisplayName(), trait.getActivationThresholds(), 1);
    }

    /**
     * Mapping in the context of a specific {@link Unit}, reporting how many copies of the
     * trait that unit counts as.
     */
    public TraitDTO apply(Unit unit, Trait trait) {
        return new TraitDTO(trait.getDisplayName(), trait.getActivationThresholds(), unit.getTraitCount(trait));
    }
}
