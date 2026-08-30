package com.tfttools.setrules;

import com.tfttools.domain.Trait;
import com.tfttools.repository.TraitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    private final Map<String, Map<Trait, Integer>> overrides;


    @Autowired
    public Set18Rules(TraitRepository traitRepository)
    {
        Map<String, Map<Trait, Integer>> tempOverrides = new HashMap<>();
        tempOverrides.put("DA_Lux18_Blackthorn", Map.of(traitRepository.getTraitByName("Blackthorn"), 2));
        tempOverrides.put("DA_Lux18_Blossom", Map.of(traitRepository.getTraitByName("Blossom"), 2));
        tempOverrides.put("DA_18_Lux_Coven", Map.of(traitRepository.getTraitByName("Coven"), 2));
        tempOverrides.put("DA_18_Lux_Elderwood", Map.of(traitRepository.getTraitByName("Elderwood"), 2));
        tempOverrides.put("DA_18_Lux_Fae", Map.of(traitRepository.getTraitByName("Fae"), 2));
        tempOverrides.put("DA_18_Lux_Inferno", Map.of(traitRepository.getTraitByName("Inferno"), 2));
        tempOverrides.put("DA_18_Lux_Moonbeam", Map.of(traitRepository.getTraitByName("Lunar"), 2));
        tempOverrides.put("DA_18_Lux_Primal", Map.of(traitRepository.getTraitByName("Primal"), 2));
        tempOverrides.put("DA_18_Lux_Sunbeam", Map.of(traitRepository.getTraitByName("Solar"), 2));

        tempOverrides.put("DA_18_ElderDragon", Map.of(traitRepository.getTraitByName("Riftbeast"), 2));

        overrides = Collections.unmodifiableMap(tempOverrides);
    }

    @Override
    public String getSetNumber()
    {
        return "18";
    }

    @Override
    public Map<String, Map<Trait, Integer>> getTraitCountOverrides()
    {
        return overrides;
    }
}
