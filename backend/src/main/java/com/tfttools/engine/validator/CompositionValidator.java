package com.tfttools.engine.validator;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;

import java.util.List;

/**
 * Interface for validating compositions against requirements
 */
public interface CompositionValidator {
    /**
     * Validate a composition and return any validation errors
     * @param composition The composition to validate
     * @param configuration The engine configuration with requirements
     * @return List of validation errors (empty if valid)
     */
    List<String> validate(Composition composition, EngineConfiguration configuration);

    /**
     * Get the priority of this validator (lower numbers run first)
     */
    default int getPriority() {
        return 100;
    }

    /**
     * Whether validation failure should discard the composition
     */
    default boolean isRequired() {
        return true;
    }
}
