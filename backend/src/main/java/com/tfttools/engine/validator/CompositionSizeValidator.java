package com.tfttools.engine.validator;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CompositionSizeValidator implements CompositionValidator {
    
    @Override
    public List<String> validate(Composition composition, EngineConfiguration configuration) {
        List<String> errors = new ArrayList<>();
        
        int maxSize = configuration.getMaxUnitsOnBoard();
        int actualSize = composition.getUnits().size();
        
        if (actualSize > maxSize) {
            errors.add(String.format("Composition too large: %d units (max: %d)", actualSize, maxSize));
        }
        
        if (actualSize == 0) {
            errors.add("Composition is empty");
        }
        
        return errors;
    }

    @Override
    public int getPriority() {
        return 5; // Check size first
    }
}
