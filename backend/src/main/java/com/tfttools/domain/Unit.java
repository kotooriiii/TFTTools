package com.tfttools.domain;

import com.tfttools.domain.repository.communitydragon.ChampionStats;
import lombok.Getter;

import java.util.List;

/**
 * Abstraction of a unit in Team Fight Tactics
 */

@Getter
public class Unit implements Nameable
{
    private final String name;
    private final int cost;
    private final Role role;
    private final ChampionStats championStats;
    private final List<Trait> traits;

    public Unit(String name, int cost, Role role, ChampionStats championStats, List<Trait> traits) {
        this.name = name;
        this.cost = cost;
        this.role = role;
        this.championStats = championStats;
        this.traits = List.copyOf(traits);
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
