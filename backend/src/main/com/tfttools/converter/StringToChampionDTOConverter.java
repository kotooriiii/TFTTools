package main.com.tfttools.converter;

import main.com.tfttools.dto.ChampionDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToChampionDTOConverter implements Converter<String, ChampionDTO> {
    @Override
    public ChampionDTO convert(String source) {
        return new ChampionDTO(source);
    }
}
