package com.tfttools.dto;

import java.util.List;

/**
 * Sanitize trait object for data transfer from business logic to requestor
 */
public class TraitDTO {

    private final String displayName;
    private final List<int[]> thresholds;

    public TraitDTO(String displayName, List<int[]> thresholds) {
        this.displayName = displayName;
        this.thresholds = thresholds;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<int[]> getThresholds() {
        return thresholds;
    }
}
