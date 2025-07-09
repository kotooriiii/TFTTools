package com.tfttools.domain;

import java.util.List;

/**
 * Abstraction of a unit in Team Fight Tactics
 */
public final class Unit {
    private final Champion champion;
    private final List<Trait> traits;
    private final int cost;

    /**
     * Constructor for unit
     * @param champion Champion name from enum
     * @param traits Champion trait(s) from enum
     */
    public Unit(Champion champion, int cost, List<Trait> traits) {
        this.champion = champion;
        this.traits = traits;
        this.cost = cost;
    }

    public Champion getChampion()
    {
        return champion;
    }

    public List<Trait> getTraits()
    {
        return traits;
    }

    public int getCost() {
        return this.cost;
    }
}
