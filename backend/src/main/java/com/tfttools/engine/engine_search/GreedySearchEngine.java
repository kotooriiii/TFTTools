package com.tfttools.engine.engine_search;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.engine.heuristic.Heuristic;
import com.tfttools.engine.manager.EngineTerminatorManager;
import com.tfttools.engine.heuristic.tiebreaker.TieBreakerScorer;

import java.util.*;
import java.util.stream.Collectors;

public class GreedySearchEngine {
    private final Heuristic heuristic;
    private final EngineState engineState;
    private final EngineTerminatorManager terminatorManager;

    public GreedySearchEngine(Heuristic heuristic, 
                             EngineState engineState,
                             EngineTerminatorManager terminatorManager) {
        this.heuristic = heuristic;
        this.engineState = engineState;
        this.terminatorManager = terminatorManager;
    }

    public List<Composition> buildCompositions() {
        while (!terminatorManager.shouldTerminate(engineState.getCurrentComp()) 
               && engineState.hasUnitsAvailable()) {

            Unit bestUnit = selectNextUnit();
            if (bestUnit == null) {
                break;
            }

            // Notify heuristic and update state
            heuristic.notifyUnitChosen(bestUnit);
            engineState.getUnitPool().remove(bestUnit);
            engineState.getCurrentComp().add(bestUnit);
        }

        return List.of(engineState.getCurrentComp());
    }

    private Unit selectNextUnit() {
        if (engineState.getUnitPool().isEmpty()) {
            return null;
        }

        // Calculate weights for all available units
        Map<Unit, Integer> unitWeights = engineState.getUnitPool().stream()
                .collect(Collectors.toMap(
                        unit -> unit,
                        heuristic::getWeight
                ));

        return selectBestUnit(unitWeights);
    }

    private Unit selectBestUnit(Map<Unit, Integer> unitWeights) {
        // Find the maximum weight - O(n)
        int maxWeight = unitWeights.values().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        // Get all units with maximum weight - O(n)
        List<Unit> topCandidates = unitWeights.entrySet().stream()
                .filter(entry -> entry.getValue() == maxWeight)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (topCandidates.size() == 1) {
            return topCandidates.get(0);
        }

        return breakTie(topCandidates);
    }

    private Unit breakTie(List<Unit> candidates) {
        List<Unit> remainingCandidates = new ArrayList<>(candidates);

        for (TieBreakerScorer tieBreaker : heuristic.getTieBreakers()) {
            if (remainingCandidates.size() <= 1) break;

            // Score all candidates with this tiebreaker
            Map<Unit, Integer> scores = remainingCandidates.stream()
                    .collect(Collectors.toMap(
                            unit -> unit,
                            tieBreaker::getScore
                    ));

            // Find the maximum score
            int maxScore = scores.values().stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0);

            // Keep only units with the maximum score
            remainingCandidates = remainingCandidates.stream()
                    .filter(unit -> scores.get(unit) == maxScore)
                    .toList();
        }

        // If we still have ties after all tiebreakers, pick randomly
        if (remainingCandidates.size() > 1) {
            Random random = new Random();
            return remainingCandidates.get(random.nextInt(remainingCandidates.size()));
        }

        return remainingCandidates.get(0);
    }
}
