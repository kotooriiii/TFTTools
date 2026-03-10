package main.com.tfttools.dto;

/**
 * Sanitize trait object for data transfer from business logic to requestor
 */
public class TraitDTO {

    private final String displayName;
    private final int[] activationThresholds;

    public TraitDTO(String displayName, int[] activationThresholds) {
        this.displayName = displayName;
        this.activationThresholds = activationThresholds;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int[] getActivationThresholds()
    {
        return activationThresholds;
    }
}
