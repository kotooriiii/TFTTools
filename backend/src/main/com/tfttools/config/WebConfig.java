package com.tfttools.config;

import com.tfttools.converter.StringToTraitConverter;
import com.tfttools.converter.StringToUnitConverter;
import com.tfttools.registry.UnitRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UnitRegistry unitRegistry;

    public WebConfig(UnitRegistry unitRegistry) {
        this.unitRegistry = unitRegistry;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToTraitConverter(unitRegistry));
        registry.addConverter(new StringToUnitConverter(unitRegistry));
    }
}
