package main.com.tfttools.dto;

/**
 * Sanitize emblem object for data transfer from business logic to requestor
 */
public class EmblemDTO {

    private final String displayName;

    public EmblemDTO(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
