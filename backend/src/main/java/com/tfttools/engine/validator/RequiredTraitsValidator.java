package com.tfttools.engine.validator;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Trait;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequiredTraitsValidator implements CompositionValidator {
    
    @Override
    public List<String> validate(Composition composition, EngineConfiguration configuration) {
        List<String> errors = new ArrayList<>();
        
        if (!configuration.hasRequiredTraits()) {
            return errors; // No required traits, validation passes
        }
        
        Map<Trait, Integer> compositionTraits = composition.getTraits();
        
        for (Map.Entry<Trait, Integer> requiredEntry : configuration.getRequiredTraits().entrySet()) {
            Trait requiredTrait = requiredEntry.getKey();
            int requiredCount = requiredEntry.getValue();
            
            int actualCount = compositionTraits.getOrDefault(requiredTrait, 0);
            
            if (actualCount < requiredCount) {
                errors.add(String.format("Insufficient %s traits: required %d, found %d", 
                    requiredTrait.getDisplayName(), requiredCount, actualCount));
            }
        }
        
        return errors;
    }

    @Override
    public int getPriority() {
        return 20; // Check after required units
    }
}
