package main.com.tfttools.mapper;

import main.com.tfttools.domain.Trait;
import main.com.tfttools.domain.Unit;
import main.com.tfttools.dto.UnitDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Maps internal {@link Unit} object to {@link UnitDTO} object
 */
@Component
public class UnitMapper implements Function<Unit, UnitDTO> {

    /**
     * Creates a new {@link UnitDTO} object from internal {@link Unit} object
     * @param unit Unit to be sanitized
     * @return Sanitized Unit object
     */
    @Override
    public UnitDTO apply(Unit unit) {
        return new UnitDTO(
                unit.getChampion().getDisplayName(),
                unit.getTraits().stream()
                        .map(Trait::getDisplayName)
                        .collect(Collectors.toSet())
        );
    }
}