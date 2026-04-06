package com.tfttools.engine.engine_filter;

import com.tfttools.domain.Unit;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ExcludedUnitFilter implements EngineFilter{
    private final Set<Unit> excludedUnits;

    public ExcludedUnitFilter(Set<Unit> excludedUnits) {
        this.excludedUnits = excludedUnits;
    }

    @Override
    public void filter(List<Unit> unitList) {
        unitList.removeAll(new HashSet<>(excludedUnits));
    }
}
