package com.tfttools.mapper;

import com.tfttools.domain.Unit;
import com.tfttools.dto.UnitDTO;
import com.tfttools.service.ChampionIconCacheService;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Maps internal {@link Unit} object to {@link UnitDTO} object
 */
@Component
public class UnitMapperSimple implements Function<Unit, UnitDTO> {
    private final TraitMapper traitMapper;
    private final ChampionIconCacheService championIconCacheService;

    public UnitMapperSimple(TraitMapper traitMapper, ChampionIconCacheService championIconCacheService) {
        this.traitMapper = traitMapper;
        this.championIconCacheService = championIconCacheService;
    }

    /**
     * Creates a new {@link UnitDTO} object from internal {@link Unit} object
     * @param unit Unit to be sanitized
     * @return Sanitized Unit object
     */
    @Override
    public UnitDTO apply(Unit unit) {
        return new UnitDTO(
                unit.getDisplayName(),
                unit.getTraits().stream().map(trait -> traitMapper.apply(unit, trait)).collect(Collectors.toSet()),
                unit.getCost(),
                championIconCacheService.getIconUrl(unit.getApiName())
        );
    }
}