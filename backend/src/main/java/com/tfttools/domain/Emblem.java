package com.tfttools.domain;

import lombok.Getter;

@Getter
public class Emblem implements Nameable
{
    private final String displayName;
    private final Trait trait;

    public Emblem(String displayName, Trait trait)
    {
        this.displayName = displayName;
        this.trait = trait;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString()
    {
        return this.displayName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;

        Emblem emblem = (Emblem) o;
        return getDisplayName().equals(emblem.getDisplayName());
    }

    @Override
    public int hashCode()
    {
        return getDisplayName().hashCode();
    }
}