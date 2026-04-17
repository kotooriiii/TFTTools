package com.tfttools.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Trait implements Nameable
{
    private final String apiName;
    private final String name;
    private final int[] thresholds;
    private List<Integer> styles;
    private boolean isUnique;

    public Trait(String apiName, String name, int[] thresholds, List<Integer> styles)
    {
        this.apiName = apiName;
        this.name = name;
        this.thresholds = thresholds;
        this.styles = styles;
        this.isUnique = apiName.endsWith("UniqueTrait");
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public int[] getActivationThresholds()
    {
        return thresholds.clone();
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    public boolean isCountable()
    {
        return !isUnique;
    }

    public boolean isActivated(int currentTraitCount)
    {
        return currentTraitCount >= thresholds[0];
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;

        Trait trait = (Trait) o;
        return getName().equals(trait.getName());
    }

    @Override
    public int hashCode()
    {
        return getName().hashCode();
    }
}
