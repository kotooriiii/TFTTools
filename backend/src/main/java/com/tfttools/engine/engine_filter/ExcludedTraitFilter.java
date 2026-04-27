package com.tfttools.engine.engine_filter;

import com.tfttools.domain.Unit;
import com.tfttools.domain.Trait;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ExcludedTraitFilter implements EngineFilter
{
    private final Set<Trait> excludedTraits;

    public ExcludedTraitFilter(Set<Trait> excludedTraits)
    {
        this.excludedTraits = new HashSet<>(excludedTraits);
    }

    @Override
    public void filter(Set<Unit> unitList)
    {
        Iterator<Unit> iterator = unitList.iterator();

        while (iterator.hasNext())
        {
            Unit next = iterator.next();

            boolean shouldRemove = next.getTraits().stream()
                    .anyMatch(excludedTraits::contains);

            if (shouldRemove)
            {
                iterator.remove();
            }
        }
    }
}
