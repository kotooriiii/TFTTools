package main.com.tfttools.graph;

import main.com.tfttools.domain.Trait;

/**
 * Represents an edge in a graph, holds trait information from Units
 */
public class TraitEdge {
    private final Trait trait;

    /**
     * Creates a new TraitEdge with given trait
     * @param trait Trait to be added
     */
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
