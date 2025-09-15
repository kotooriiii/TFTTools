package com.tfttools.engine.enginefilter;

import com.tfttools.domain.Champion;
import com.tfttools.domain.Unit;

import java.util.List;

public interface EngineFilter {
    List<Unit> filter(List<Unit> championList);
}
