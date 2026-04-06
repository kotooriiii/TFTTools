package com.tfttools.domain;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Composition
{
    private final List<Unit> units;
    private final Map<Trait, Integer> traits;


    public Composition()
    {
        this(new ArrayList<>());
    }

    public Composition(List<Unit> units)
    {
        this.units = units;
        this.traits = new HashMap<>();

        units.forEach(unit ->
                unit.getTraits().forEach(trait ->
                        traits.put(trait, traits.getOrDefault(trait, 0) + 1)));
    }

    //Copy constructor
    public Composition(@NotNull Composition original)
    {
        this(new ArrayList<>(original.units));
    }

    public List<Unit> getUnits()
    {
        return new ArrayList<>(units);
    }

    public Map<Trait, Integer> getTraits()
    {
        return new HashMap<>(traits);
    }

    public int size()
    {
        return units.size();
    }

    public void add(Unit unit)
    {
        if(this.units.add(unit))
            unit.getTraits().forEach(trait ->
                traits.put(trait, traits.getOrDefault(trait, 0) + 1));
    }

    public void addAll(Collection<Unit> units)
    {
        units.forEach(this::add);
    }

    public void remove(Unit unit)
    {
        if(this.units.remove(unit))
            unit.getTraits().forEach(trait ->
                traits.computeIfPresent(trait, (key, value) -> value - 1 == 0 ? null : value - 1));
    }

    public void clear()
    {
        units.clear();
        traits.clear();
    }

    public boolean contains(Unit unit)
    {
        return this.units.contains(unit);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (!(obj instanceof Composition)) return false;
        Composition other = (Composition) obj;
        return new HashSet<>(this.units).containsAll(other.units) && this.traits.equals(other.traits);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(units, traits);
    }
}
