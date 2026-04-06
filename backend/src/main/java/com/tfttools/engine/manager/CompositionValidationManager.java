package com.tfttools.engine.manager;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.engine.validator.*;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class CompositionValidationManager {
    private final List<CompositionValidator> validators;

    public CompositionValidationManager() {
        this.validators = Arrays.asList(
            new CompositionSizeValidator(),
            new RequiredUnitsValidator(),
            new RequiredTraitsValidator(),
            new ExcludedUnitsValidator(),
            new ExcludedTraitsValidator()
        );
        
        // Sort by priority
        this.validators.sort(Comparator.comparingInt(CompositionValidator::getPriority));
    }

    /**
     * Validate a single composition
     */
    public ValidationResult validateComposition(Composition composition, EngineConfiguration configuration) {
        List<String> allErrors = new ArrayList<>();
        List<String> allWarnings = new ArrayList<>();
        
        for (CompositionValidator validator : validators) {
            List<String> errors = validator.validate(composition, configuration);
            
            if (!errors.isEmpty()) {
                if (validator.isRequired()) {
                    allErrors.addAll(errors);
                } else {
                    allWarnings.addAll(errors);
                }
            }
        }
        
        return new ValidationResult(allErrors.isEmpty(), allErrors, allWarnings);
    }

    /**
     * Filter compositions, keeping only valid ones and logging issues
     */
    public List<Composition> filterValidCompositions(List<Composition> compositions, EngineConfiguration configuration) {
        List<Composition> validCompositions = new ArrayList<>();
        
        for (Composition composition : compositions) {
            ValidationResult result = validateComposition(composition, configuration);
            
            if (result.isValid()) {
                validCompositions.add(composition);
                
                // Log warnings if any
                if (!result.getWarnings().isEmpty()) {
                    System.out.printf("Composition warnings: %s%n", 
                        String.join(", ", result.getWarnings()));
                }
            } else {
                // Log validation failures
                System.out.printf("Invalid composition discarded: %s%n", 
                    String.join(", ", result.getErrors()));
            }
        }
        
        return validCompositions;
    }

    /**
     * Validation result container
     */
    @Getter
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        private final List<String> warnings;

        public ValidationResult(boolean valid, List<String> errors, List<String> warnings) {
            this.valid = valid;
            this.errors = new ArrayList<>(errors);
            this.warnings = new ArrayList<>(warnings);
        }
    }
}
