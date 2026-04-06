package com.tfttools.engine.engine_terminator;

import com.tfttools.domain.Composition;

public class CompSizeEngineTerminator implements EngineTerminator{
    private final int tactitionLevel;
    private final int numCrowns;

    public CompSizeEngineTerminator(int tactitionLevel, int numCrowns) {
        this.tactitionLevel = tactitionLevel;
        this.numCrowns = numCrowns;
    }

    public boolean shouldTerminate(Composition currentComposition) {
        // the current comp has exceeded the maximum number of playable units on board
        return tactitionLevel + numCrowns <= currentComposition.size();
    }
}
