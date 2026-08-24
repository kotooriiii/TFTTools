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
public class UnitMapperDetailed implements Function<Unit, UnitDTO> {
    private final TraitMapper traitMapper;
    private final ChampionIconCacheService championIconCacheService;

    public UnitMapperDetailed(TraitMapper traitMapper, ChampionIconCacheService championIconCacheService) {
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
                unit.getTraits().stream().map(traitMapper).collect(Collectors.toSet()),
                unit.getChampionStats(),
                unit.getRole(),
                unit.getCost(),
                championIconCacheService.getIconUrl(unit.getApiName())
        );
    }
}