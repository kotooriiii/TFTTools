package com.tfttools.graph;

import com.tfttools.domain.Trait;

public class TraitEdge {
    private final Trait trait;

    public TraitEdge(Trait trait) {
        this.trait = trait;
    }

    public Trait getTrait() {
        return trait;
    }

    @Override
    public String toString() {
        return trait.name();
    }
}
