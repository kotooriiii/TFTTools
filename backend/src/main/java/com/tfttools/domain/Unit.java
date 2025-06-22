package com.tfttools.domain;

import java.util.List;

public final class Unit {
    private final Champion champion;
    private final List<Trait> traits;

    //todo add unit cost

    public Unit(Champion champion, List<Trait> traits) {
        this.champion = champion;
        this.traits = traits;
    }

    public Champion getChampion()
    {
        return champion;
    }

    public List<Trait> getTraits()
    {
        return traits;
    }
}
