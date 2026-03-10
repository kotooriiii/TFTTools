package com.tfttools.engine.engineterminator;

import com.tfttools.domain.Unit;

import java.util.Set;

public class CompSizeEngineTerminator implements EngineTerminator{
    private final int tactitionLevel;
    private final int numCrowns;

    public CompSizeEngineTerminator(int tactitionLevel, int numCrowns) {
        this.tactitionLevel = tactitionLevel;
        this.numCrowns = numCrowns;
    }

    public boolean getCondition(Set<Unit> comp) {
        return tactitionLevel + numCrowns > comp.size();
    }
}
