package com.tfttools.dto;

/**
 * Sanitize trait object for data transfer from business logic to requestor
 */
public class TraitDTO {

    private final String displayName;

    public TraitDTO(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
