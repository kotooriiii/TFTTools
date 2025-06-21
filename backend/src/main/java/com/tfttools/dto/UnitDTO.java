package com.tfttools.dto;

import java.util.Set;

public class UnitDTO {
    private String champion;
    private Set<String> traits;

    public UnitDTO(String champion, Set<String> traits) {
        this.champion = champion;
        this.traits = traits;
    }

    public String getChampion() {
        return champion;
    }

    public Set<String> getTraits() {
        return traits;
    }
}
