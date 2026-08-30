package com.tfttools.setrules;

import org.springframework.stereotype.Component;

/**
 * Set-specific overrides for TFT Set 18. Empty scaffold — Community Dragon does not expose
 * non-standard variant naming or trait-count multipliers, so entries here have to be added by
 * hand as they're confirmed in-game (e.g. via {@link #resolveVariantBaseName} or
 * {@link #getTraitCountOverrides}). Confirm "18" still matches
 * {@code TFTSetContextService.getCurrentSetNumber()} for the live set before relying on this.
 */
@Component
public class Set18Rules implements SetSpecificRules
{
    @Override
    public String getSetNumber()
    {
        return "18";
    }
}
