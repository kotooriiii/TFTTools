package com.tfttools.engine.validator;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RequiredUnitsValidator implements CompositionValidator {
    
    @Override
    public List<String> validate(Composition composition, EngineConfiguration configuration) {
        List<String> errors = new ArrayList<>();
        
        if (!configuration.hasRequiredUnits()) {
            return errors; // No required units, validation passes
        }
        
        Set<Unit> compositionUnits = Set.copyOf(composition.getUnits());
        
        for (Unit requiredUnit : configuration.getRequiredUnits()) {
            if (!compositionUnits.contains(requiredUnit)) {
                errors.add("Missing required unit: " + requiredUnit.getDisplayName());
            }
        }
        
        return errors;
    }

    @Override
    public int getPriority() {
        return 10; // High priority - check required units first
    }
}
