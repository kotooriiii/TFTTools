package com.tfttools.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Trait implements Nameable
{
    private final String name;
    private final int[] thresholds;
    private List<Integer> styles;

    public Trait(String name, int[] thresholds, List<Integer> styles)
    {
        this.name = name;
        this.thresholds = thresholds;
        this.styles = styles;
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
        return styles.stream().allMatch(style -> style != 4);
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
