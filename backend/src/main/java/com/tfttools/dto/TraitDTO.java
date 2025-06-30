package com.tfttools.dto;

import com.tfttools.domain.Trait;

/**
 * Sanitize trait object for data transfer from business logic to requestor
 */
public class TraitDTO {

    private final Trait trait;
    private final String displayName;

    public TraitDTO(Trait trait) {
        this.trait = trait;
        this.displayName = trait.toString();
    }

    public Trait getTrait() {
        return trait;
    }

    public String getDisplayName() {
        return displayName;
    }
}
