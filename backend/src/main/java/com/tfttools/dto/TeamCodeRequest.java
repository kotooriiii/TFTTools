package com.tfttools.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Ordered list of unit apiNames to encode into a team code - no engine-generated
 * {@code Composition} required. Backs both the My Comps expanded view and Team Builder saves.
 */
public record TeamCodeRequest(
        @NotEmpty List<String> unitApiNames
) {
}
