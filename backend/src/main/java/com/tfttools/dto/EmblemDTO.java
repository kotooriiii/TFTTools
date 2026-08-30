package com.tfttools.dto;

/**
 * Sanitize emblem object for data transfer from business logic to requestor
 */
public class EmblemDTO {

    private final String apiName;
    private final String displayName;

    public EmblemDTO(String apiName, String displayName) {
        this.apiName = apiName;
        this.displayName = displayName;
    }

    public String getApiName() {
        return apiName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
