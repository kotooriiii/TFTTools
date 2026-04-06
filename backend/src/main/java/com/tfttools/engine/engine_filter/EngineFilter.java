package com.tfttools.engine.engine_filter;

import com.tfttools.domain.Unit;

import java.util.List;

public interface EngineFilter {
    void filter(List<Unit> championList);
}
