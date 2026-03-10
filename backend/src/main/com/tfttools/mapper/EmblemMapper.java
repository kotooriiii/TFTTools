package main.com.tfttools.mapper;

import main.com.tfttools.domain.Emblem;
import main.com.tfttools.domain.Trait;
import main.com.tfttools.dto.EmblemDTO;
import main.com.tfttools.dto.TraitDTO;
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
