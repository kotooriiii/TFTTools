package com.tfttools.dto;

/**
 * Sanitize trait object for data transfer from business logic to requestor
 */
public class TraitDTO {

    private final String displayName;
    private final int[] thresholds;

    public TraitDTO(String displayName, int[] thresholds) {
        this.displayName = displayName;
        this.thresholds = thresholds;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int[] getThresholds() {
        return thresholds;
    }
}
