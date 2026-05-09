package com.tfttools.engine.engine_search;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.engine.heuristic.Heuristic;
import com.tfttools.engine.manager.EngineTerminatorManager;
import com.tfttools.util.CompositionUtils;

import java.util.*;

public class BeamSearchEngine {
    private final Heuristic heuristic;
    private final EngineState initialEngineState;
    private final EngineTerminatorManager terminatorManager;
    private final int beamWidth;

    public BeamSearchEngine(Heuristic heuristic, 
                           EngineState engineState,
                           EngineTerminatorManager terminatorManager, 
                           int beamWidth) {
        this.heuristic = heuristic;
        this.initialEngineState = engineState;
        this.terminatorManager = terminatorManager;
        this.beamWidth = beamWidth;
    }

    public List<Composition> buildCompositions() {
        List<BeamState> currentBeam = new ArrayList<>();
        currentBeam.add(new BeamState(initialEngineState.copy()));

        while (!currentBeam.isEmpty() && !allBeamStatesComplete(currentBeam)) {
            List<BeamState> nextBeam = new ArrayList<>();

            for (BeamState currentState : currentBeam) {
                if (isComplete(currentState)) {
                    nextBeam.add(currentState);
                    continue;
                }

                // Get top units for this state
                List<Unit> topUnits = getTopKUnits(currentState.engineState, beamWidth);

                // Create new states by adding each top unit
                for (Unit unit : topUnits) {
                    BeamState newState = expandState(currentState, unit);
                    if (newState != null) {
                        nextBeam.add(newState);
                    }
                }
            }

            // Keep only top beamWidth states based on heuristic evaluation
            currentBeam = selectTopBeamStates(nextBeam, beamWidth);
        }

        // Convert beam states to compositions
        return currentBeam.stream()
                .filter(this::isComplete)
                .map(state -> state.engineState.getCurrentComp())
                .toList();
    }

    private List<Unit> getTopKUnits(EngineState engineState, int k) {
        Set<Unit> availableUnits = engineState.getUnitPool();

        if (availableUnits.isEmpty()) {
            return Collections.emptyList();
        }

        // Calculate weights for all available units
        Map<Unit, Integer> unitWeights = new HashMap<>();
        for (Unit unit : availableUnits) {
            int weight = heuristic.getWeight(unit);
            unitWeights.put(unit, weight);
        }

        // Sort units by weight (descending) and return top k
        return unitWeights.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(k)
                .map(Map.Entry::getKey)
                .toList();
    }

    private BeamState expandState(BeamState currentState, Unit unitToAdd) {
        try {
            EngineState newEngineState = currentState.engineState.copy();

            // Add the unit to the composition
            newEngineState.getUnitPool().remove(unitToAdd);
            newEngineState.getCurrentComp().add(unitToAdd);

            return new BeamState(newEngineState);
        } catch (Exception e) {
            // Unit couldn't be added (constraints violated)
            return null;
        }
    }

    private List<BeamState> selectTopBeamStates(List<BeamState> candidates, int maxBeamWidth) {
        if (candidates.size() <= maxBeamWidth) {
            return candidates;
        }

        // Sort by composition quality/heuristic score
        candidates.sort((state1, state2) -> {
            double score1 = evaluateCompositionQuality(state1);
            double score2 = evaluateCompositionQuality(state2);
            return Double.compare(score2, score1); // Descending order
        });

        return candidates.subList(0, maxBeamWidth);
    }

    private double evaluateCompositionQuality(BeamState state) {
        return CompositionUtils.INSTANCE.getActivatedTraits(state.engineState.getCurrentComp()).size();
    }

    private boolean isComplete(BeamState state) {
        return terminatorManager.shouldTerminate(state.engineState.getCurrentComp());
    }

    private boolean allBeamStatesComplete(List<BeamState> beam) {
        return beam.stream().allMatch(this::isComplete);
    }

    private static class BeamState {
        final EngineState engineState;

        BeamState(EngineState engineState) {
            this.engineState = engineState;
        }
    }
}
