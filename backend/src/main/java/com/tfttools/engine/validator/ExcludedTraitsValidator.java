package com.tfttools.engine.validator;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Trait;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExcludedTraitsValidator implements CompositionValidator {
    
    @Override
    public List<String> validate(Composition composition, EngineConfiguration configuration) {
        List<String> errors = new ArrayList<>();
        
        if (configuration.getExcludedTraits().isEmpty()) {
            return errors; // No excluded traits, validation passes
        }
        
        Set<Trait> compositionTraits = composition.getTraits().keySet();
        Set<Trait> excludedTraits = Set.copyOf(configuration.getExcludedTraits());
        
        for (Trait trait : compositionTraits) {
            if (excludedTraits.contains(trait)) {
                errors.add("Contains excluded trait: " + trait.getDisplayName());
            }
        }
        
        return errors;
    }

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public boolean isRequired() {
        return false; // Maybe warn but don't discard for this
    }
}
