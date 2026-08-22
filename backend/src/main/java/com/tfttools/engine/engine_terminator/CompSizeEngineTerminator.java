package com.tfttools.engine.engine_terminator;

import com.tfttools.domain.Composition;

public class CompSizeEngineTerminator implements EngineTerminator{
    private final int tacticianLevel;
    private final int numCrowns;

    public CompSizeEngineTerminator(int tacticianLevel, int numCrowns) {
        this.tacticianLevel = tacticianLevel;
        this.numCrowns = numCrowns;
    }

    public boolean shouldTerminate(Composition currentComposition) {
        // the current comp has exceeded the maximum number of playable units on board
        return tacticianLevel + numCrowns <= currentComposition.size();
    }
}
