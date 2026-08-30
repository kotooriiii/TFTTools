package com.tfttools.domain;

import com.tfttools.domain.communitydragon.ChampionStats;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstraction of a unit in Team Fight Tactics
 */

@Getter
public class Unit implements Nameable
{
    private final String apiName;
    private final String name;
    private final int cost;
    private final Role role;
    private final ChampionStats championStats;
    private final List<Trait> traits;
    private final Map<Trait, Integer> traitCounts;
    private final String tileIconPath;

    public Unit(String apiName, String name, int cost, Role role, ChampionStats championStats, List<Trait> traits, String tileIconPath) {
        this(apiName, name, cost, role, championStats, traits, tileIconPath, Map.of());
    }

    /**
     * @param traitCountOverrides how many copies of a trait this unit counts as, keyed by {@link Trait}.
     *                            Traits not present here default to counting as 1 (set-specific quirks,
     *                            e.g. a variant champion counting double for its own trait, are supplied
     *                            via {@code com.tfttools.setrules.SetSpecificRules}).
     */
    public Unit(String apiName, String name, int cost, Role role, ChampionStats championStats, List<Trait> traits, String tileIconPath, Map<Trait, Integer> traitCountOverrides) {
        this.apiName = apiName;
        this.name = name;
        this.cost = cost;
        this.role = role;
        this.championStats = championStats;
        this.traits = List.copyOf(traits);
        this.tileIconPath = tileIconPath;

        Map<Trait, Integer> counts = new HashMap<>();
        for (Trait trait : this.traits) {
            counts.put(trait, traitCountOverrides.getOrDefault(trait, 1));
        }
        this.traitCounts = Map.copyOf(counts);
    }

    /**
     * How many copies of {@code trait} this unit counts as (normally 1; 0 if the unit doesn't have the trait).
     */
    public int getTraitCount(Trait trait) {
        return traitCounts.getOrDefault(trait, 0);
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
