package com.tfttools.dto;

import com.tfttools.domain.Champion;

/**
 * Sanitize champion object for data transfer from business logic to requestor
 */
public class ChampionDTO {

    private final String displayName;

    public ChampionDTO(Champion champion) {
        this.displayName = champion.toString();
    }

    public String getDisplayName() {
        return displayName;
    }
}
