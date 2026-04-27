package com.tfttools.engine.engine_filter;

import com.tfttools.domain.Unit;

import java.util.List;
import java.util.Set;

public interface EngineFilter {
    void filter(Set<Unit> championList);
}
