package com.tfttools.engine.engineterminator;

import com.tfttools.domain.Unit;

import java.util.Set;

public interface EngineTerminator {
    boolean getCondition(Set<Unit> comp);
}
