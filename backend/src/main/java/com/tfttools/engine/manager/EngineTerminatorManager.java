package com.tfttools.engine.manager;

import com.tfttools.engine.engineterminator.EngineTerminator;
import com.tfttools.engine.engineterminator.*;

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
}
