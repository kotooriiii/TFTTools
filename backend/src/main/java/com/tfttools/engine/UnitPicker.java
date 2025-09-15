package com.tfttools.engine;

import com.tfttools.domain.Unit;
import com.tfttools.engine.manager.EngineTieBreakerManager;
import com.tfttools.engine.tiebreaker.TieBreaker;

import java.util.*;

public class UnitPicker {
    private final EngineState engineState;
    private final PriorityQueue<Map.Entry<Unit, Integer>> rankings;
    private final EngineTieBreakerManager engineTieBreakerManager;

    public UnitPicker(EngineState engineState, Map<Unit, Integer> unitWeights, List<Set<Unit>> previousComps) {
        this.engineState = engineState;
        this.rankings = new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        initRankings(unitWeights);
        if (engineState.getCore().size() < engineState.getTactitionLevel() / 3) {
            for (int i = 0; i < engineState.getTactitionLevel() - engineState.getCore().size(); i++) {
                engineState.addToCore(this.rankings.poll().getKey());
            }
        }

        this.engineTieBreakerManager = new EngineTieBreakerManager(engineState, previousComps);
    }

    private void initRankings(Map<Unit, Integer> unitWeights) {
        this.rankings.addAll(unitWeights.entrySet());
    }

    public Unit getNextUnit() {
        List<Unit> tieCandidates = new ArrayList<>();

        Map.Entry<Unit, Integer> first = rankings.poll();
        tieCandidates.add(first.getKey());

        while (rankings.peek().getValue().intValue() == first.getValue().intValue()) {
            tieCandidates.add(rankings.poll().getKey());
        }

        if (tieCandidates.size() > 1) {
            for (TieBreaker tieBreaker : engineTieBreakerManager.getTieBreakers()) {
                return tieBreaker.chooseUnit(tieCandidates);
            }
        }

        return tieCandidates.get(0);
    }
}
