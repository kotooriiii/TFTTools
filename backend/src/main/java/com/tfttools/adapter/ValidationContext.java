package com.tfttools.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to collect validation errors
 */
public  class ValidationContext {
    private final List<String> errors = new ArrayList<>();

    void addError(String error) {
        errors.add(error);
    }

    void throwIfErrors() {
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + String.join(", ", errors));
        }
    }
}