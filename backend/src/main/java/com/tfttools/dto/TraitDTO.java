package com.tfttools.dto;

/**
 * Sanitize trait object for data transfer from business logic to requestor
 */
public class TraitDTO {

    private final String displayName;
    private final int[] activationThresholds;
    private final int count;

    public TraitDTO(String displayName, int[] activationThresholds, int count) {
        this.displayName = displayName;
        this.activationThresholds = activationThresholds;
        this.count = count;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int[] getActivationThresholds()
    {
        return activationThresholds;
    }

    /**
     * How many copies of this trait the owning unit counts as (normally 1).
     */
    public int getCount()
    {
        return count;
    }

    @Override
    public String toString()
    {
        return getDisplayName();
    }
}
