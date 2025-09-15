package com.tfttools.engine.manager;

import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.engine.tiebreaker.DiversityTieBreaker;
import com.tfttools.engine.tiebreaker.TieBreaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EngineTieBreakerManager {
    private final EngineState engineState;
    private final List<TieBreaker> tieBreakers;
    private final List<Set<Unit>> previousComps;

    public EngineTieBreakerManager(EngineState engineState, List<Set<Unit>> previousComps) {
        this.engineState = engineState;
        this.tieBreakers = new ArrayList<>();
        this.previousComps = previousComps;

        initTieBreakers();
    }

    private void initTieBreakers() {
        DiversityTieBreaker diversityTieBreaker = new DiversityTieBreaker(previousComps);

        this.tieBreakers.add(diversityTieBreaker);
    }

    public List<TieBreaker> getTieBreakers() {
        return tieBreakers;
    }
}
