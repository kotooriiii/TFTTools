package com.tfttools.engine.heuristic;

import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.engine.heuristic.tiebreaker.TieBreakerScorer;

import java.util.*;
import java.util.stream.Collectors;

public class UnitSelector
{
    private final Heuristic heuristic;

    public UnitSelector(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * Pick the next best unit and remove it from the available pool
     */
    public void pickNextUnit(EngineState engineState) {
        if (engineState.getUnitPool().isEmpty()) return;

        // Calculate weights for all available units
        Map<Unit, Integer> unitWeights = engineState.getUnitPool().stream()
                .collect(Collectors.toMap(
                        unit -> unit,
                        heuristic::getWeight
                ));

        // Find the best unit(s)
        Unit bestUnit = selectBestUnit(unitWeights);

        heuristic.notifyUnitChosen(bestUnit);

        // Remove from available pool
        engineState.getUnitPool().remove(bestUnit);
        engineState.getCurrentComp().add(bestUnit);
    }



    // O(2n) .. do not do pq.. technicaly its also O(n) but let k = number of ties. so it would be something more like O(n) to build the maxheap + O(klogn) for every tie
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
                    .collect(Collectors.toList());
        }

        // If we still have ties after all tiebreakers, pick randomly
        if (remainingCandidates.size() > 1) {
            Random random = new Random();
            return remainingCandidates.get(random.nextInt(remainingCandidates.size()));
        }

        return remainingCandidates.get(0);
    }
}