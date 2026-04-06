package com.tfttools.engine.validator;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExcludedUnitsValidator implements CompositionValidator {
    
    @Override
    public List<String> validate(Composition composition, EngineConfiguration configuration) {
        List<String> errors = new ArrayList<>();
        
        if (configuration.getExcludedUnits().isEmpty()) {
            return errors; // No excluded units, validation passes
        }
        
        Set<Unit> compositionUnits = Set.copyOf(composition.getUnits());
        Set<Unit> excludedUnits = Set.copyOf(configuration.getExcludedUnits());
        
        for (Unit unit : compositionUnits) {
            if (excludedUnits.contains(unit)) {
                errors.add("Contains excluded unit: " + unit.getDisplayName());
            }
        }
        
        return errors;
    }

    @Override
    public int getPriority() {
        return 30;
    }
}
