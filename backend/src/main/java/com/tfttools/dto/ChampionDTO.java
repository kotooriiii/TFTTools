package com.tfttools.dto;

/**
 * Sanitize champion object for data transfer from business logic to requestor
 */
public class ChampionDTO {

    private final String displayName;

    public ChampionDTO(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
