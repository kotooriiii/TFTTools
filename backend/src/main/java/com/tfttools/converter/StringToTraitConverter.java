package com.tfttools.converter;

import com.tfttools.domain.Trait;
import com.tfttools.repository.TraitRepository;
import org.springframework.core.convert.converter.Converter;

public class StringToTraitConverter implements Converter<String, Trait> {
    private final TraitRepository traitRepository;

    public StringToTraitConverter(TraitRepository traitRepository) {
        this.traitRepository = traitRepository;
    }

    @Override
    public Trait convert(String source) {
        return this.traitRepository.getTraitByName(source);
    }
}
