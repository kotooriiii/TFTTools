package com.tfttools.dto;

import com.tfttools.domain.Champion;

/**
 * Sanitize champion object for data transfer from business logic to requestor
 */
public class ChampionDTO {

    private final Champion champion;
    private final String displayName;

    public ChampionDTO(Champion champion) {
        this.champion = champion;
        this.displayName = champion.toString();
    }

    public Champion getChampion() {
        return champion;
    }

    public String getDisplayName() {
        return displayName;
    }
}
