package com.tfttools.engine.manager;

import com.tfttools.domain.Composition;
import com.tfttools.engine.engine_terminator.EngineTerminator;
import com.tfttools.engine.engine_terminator.*;

import java.util.ArrayList;
import java.util.List;

public class EngineTerminatorManager {
    private final List<EngineTerminator> engineTerminators;

    public EngineTerminatorManager(int tactitionLevel, int crowns) {
        this.engineTerminators = new ArrayList<>();
        CompSizeEngineTerminator compSizeEngineTerminator = new CompSizeEngineTerminator(tactitionLevel, crowns);
        this.engineTerminators.add(compSizeEngineTerminator);
    }

    public List<EngineTerminator> getEngineTerminators() {
        return engineTerminators;
    }

    public boolean shouldTerminate(Composition comp)
    {
        for (EngineTerminator terminator : getEngineTerminators()) {
            if (terminator.shouldTerminate(comp)) {
                return true;
            }
        }

        return false;
    }
}
