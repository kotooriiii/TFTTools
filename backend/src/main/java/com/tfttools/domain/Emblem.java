package com.tfttools.domain;

import lombok.Getter;

@Getter
public class Emblem implements Nameable
{
    private final String apiName;
    private final String displayName;
    private final Trait trait;

    public Emblem(String apiName, String displayName, Trait trait)
    {
        this.apiName = apiName;
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
        return getApiName().equals(emblem.getApiName());
    }

    @Override
    public int hashCode()
    {
        return getApiName().hashCode();
    }
}