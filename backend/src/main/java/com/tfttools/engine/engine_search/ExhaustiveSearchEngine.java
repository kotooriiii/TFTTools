package com.tfttools.engine.engine_search;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.engine.manager.EngineTerminatorManager;
import com.tfttools.engine.heuristic.Heuristic;
import com.tfttools.util.CompositionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ExhaustiveSearchEngine {
    private final Heuristic heuristic;
    private final EngineState initialEngineState;
    private final EngineTerminatorManager terminatorManager;

    public ExhaustiveSearchEngine(Heuristic heuristic, 
                                 EngineState engineState,
                                 EngineTerminatorManager terminatorManager) {
        this.heuristic = heuristic;
        this.initialEngineState = engineState;
        this.terminatorManager = terminatorManager;
    }

    public List<Composition> buildCompositions() {
        List<SearchState> currentStates = new ArrayList<>();
        currentStates.add(new SearchState(initialEngineState.copy()));

        while (!currentStates.isEmpty() && !allStatesComplete(currentStates)) {
            List<SearchState> nextStates = new ArrayList<>();

            for (SearchState currentState : currentStates) {
                if (isComplete(currentState)) {
                    nextStates.add(currentState);
                    continue;
                }

                // Get all units with maximum weight for this state
                List<Unit> maxWeightUnits = getUnitsWithMaxWeight(currentState.engineState);

                // Create new states by adding each max weight unit
                for (Unit unit : maxWeightUnits) {
                    SearchState newState = expandState(currentState, unit);
                    if (newState != null) {
                        nextStates.add(newState);
                    }
                }
            }

            // Keep all states (no pruning based on beam width)
            currentStates = nextStates;
        }

        // Convert search states to compositions and remove duplicates
        return currentStates.stream()
                .filter(this::isComplete)
                .map(state -> state.engineState.getCurrentComp())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Unit> getUnitsWithMaxWeight(EngineState engineState) {
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

        // Find the maximum weight
        int maxWeight = unitWeights.values().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        // Return all units with maximum weight
        return unitWeights.entrySet().stream()
                .filter(entry -> entry.getValue() == maxWeight)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private SearchState expandState(SearchState currentState, Unit unitToAdd) {
        try {
            EngineState newEngineState = currentState.engineState.copy();

            // Add the unit to the composition
            newEngineState.getUnitPool().remove(unitToAdd);
            newEngineState.getCurrentComp().add(unitToAdd);

            return new SearchState(newEngineState);
        } catch (Exception e) {
            // Unit couldn't be added (constraints violated)
            return null;
        }
    }

    private boolean isComplete(SearchState state) {
        return terminatorManager.shouldTerminate(state.engineState.getCurrentComp());
    }

    private boolean allStatesComplete(List<SearchState> states) {
        return states.stream().allMatch(this::isComplete);
    }

    private static class SearchState {
        final EngineState engineState;

        SearchState(EngineState engineState) {
            this.engineState = engineState;
        }
    }
}
