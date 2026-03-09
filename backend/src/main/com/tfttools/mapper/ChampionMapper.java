package main.com.tfttools.mapper;

import main.com.tfttools.domain.Champion;
import main.com.tfttools.dto.ChampionDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Maps internal {@link Champion} object to {@link ChampionDTO} object
 */
@Component
public class ChampionMapper implements Function<Champion, ChampionDTO> {

    @Override
    public ChampionDTO apply(Champion champion) {
        return new ChampionDTO(champion.getDisplayName());
    }
}
