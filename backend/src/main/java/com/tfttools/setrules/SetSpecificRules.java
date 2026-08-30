package com.tfttools.setrules;

import com.tfttools.domain.Trait;

import java.util.Map;
import java.util.Optional;

/**
 * Extension point for TFT-set-specific unit behavior that Community Dragon's data does not expose
 * structurally (e.g. non-standard variant naming, units that count as more than one copy of a trait).
 * Implementations are registered per set number via {@link SetSpecificRulesRegistry} and only apply
 * when that set is the currently active one.
 */
public interface SetSpecificRules
{
    /**
     * The TFT set number this ruleset applies to (matches {@code TFTSetContextService.getCurrentSetNumber()}).
     */
    String getSetNumber();

    /**
     * Resolves the base champion display name for a variant champion's display name
     * (e.g. "Lux (Blossom)" -> "Lux"), when this set's naming convention doesn't match
     * the generic "Base (Variant)" pattern. Return empty to fall back to the generic pattern.
     */
    default Optional<String> resolveVariantBaseName(String variantDisplayName)
    {
        return Optional.empty();
    }

    /**
     * Per-unit trait count overrides for this set: championApiName -> traitName -> count.
     * traitName must match whatever {@code TraitRepository.getTraitByName} expects (the same
     * trait name string Community Dragon reports on the champion). Units/traits not present
     * here default to counting as 1.
     */
    default Map<String, Map<Trait, Integer>> getTraitCountOverrides()
    {
        return Map.of();
    }
}
