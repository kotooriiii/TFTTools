package com.tfttools.config;

import com.tfttools.converter.StringToTraitConverter;
import com.tfttools.converter.StringToUnitConverter;
import com.tfttools.repository.TraitRepository;
import com.tfttools.repository.UnitRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UnitRepository unitRepository;
    private final TraitRepository traitRepository;

    public WebConfig(UnitRepository unitRepository, TraitRepository traitRepository)
    {
        this.unitRepository = unitRepository;
        this.traitRepository = traitRepository;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToTraitConverter(traitRepository));
        registry.addConverter(new StringToUnitConverter(unitRepository));
    }
}
