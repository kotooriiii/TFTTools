package com.tfttools.graph;

import com.tfttools.domain.Champion;

import java.util.Objects;

/**
 * Class to wrap champions with a heuristic weight
 */
public class Neighbors {
    private final Champion champion;
    private final float weight;

    public Neighbors(Champion champion, float weight) {
        this.champion = champion;
        this.weight = weight;
    }

    public Champion getChampion() {
        return champion;
    }

    public float getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Neighbors neighbors = (Neighbors) o;
        return getWeight() == neighbors.getWeight() && getChampion() == neighbors.getChampion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChampion(), getWeight());
    }
}
