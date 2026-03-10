package com.tfttools.engine;

import com.tfttools.domain.Unit;
import com.tfttools.engine.enginefilter.EngineFilter;
import com.tfttools.engine.enginefilter.RequiredUnitFilter;
import com.tfttools.engine.engineterminator.EngineTerminator;
import com.tfttools.engine.manager.EngineFilterManager;
import com.tfttools.engine.manager.EngineHeuristicManager;
import com.tfttools.engine.manager.EngineTerminatorManager;
import com.tfttools.engine.manager.Manager;

import java.util.*;

public class TFTEngine {
    private final EngineHeuristicManager engineHeuristicManager;
    private final EngineTerminatorManager engineTerminatorManager;
    private final EngineFilterManager engineFilterManager;
    private final EngineState engineState;
    private final List<Set<Unit>> comps;
    private List<Unit> unitPool;
    /**
     * idea of having some amount of core units to create comps around to guarantee uniqueness, obviously place all required units if none then the highest ranked units
     * number of core units are the max(number of required units, (tactition level + crowns) / 3), if there are required units < (tactition level + crowns) / 3 then fill in core with highest ranked units
     */


    public TFTEngine(EngineHeuristicManager engineHeuristicManager, EngineTerminatorManager engineTerminatorManager, EngineFilterManager engineFilterManager, EngineState engineState, List<Set<Unit>> comps, List<Unit> unitPool) {
        this.engineHeuristicManager = engineHeuristicManager;
        this.engineTerminatorManager = engineTerminatorManager;
        this.engineFilterManager = engineFilterManager;
        this.engineState = engineState;
        this.comps = comps;
        this.unitPool = unitPool;

        filterUnitPool();
    }

    public TFTEngine(Manager manager) {
        this.engineHeuristicManager = manager.getEngineHeuristicManager();
        this.engineTerminatorManager = manager.getEngineTerminatorManager();
        this.engineFilterManager = manager.getEngineFilterManager();
        this.engineState = manager.getEngineStateManager().getEngineState();
        this.comps = manager.getComps();
        this.unitPool = manager.getUnitPool();

        filterUnitPool();
    }

    private void filterUnitPool() {
        List<EngineFilter> filters = engineFilterManager.getEngineFilters();

        for (EngineFilter filter : filters) {
            if (filter.getClass().equals(RequiredUnitFilter.class)) {
                for (Unit unit : ((RequiredUnitFilter) filter).getRequiredUnits()) {
                    engineState.addUnitToComp(unit);
                }
            }

            this.unitPool = filter.filter(unitPool);
        }
    }

    public List<Set<Unit>> buildComps(int numComps) {
        for (int i=0; i<numComps;i++) {
            HashSet<Unit> unitPoolCopy = new HashSet<>(unitPool);
            buildComp(unitPoolCopy);
        }

        return comps;
    }

    private void buildComp(Set<Unit> unitPoolCopy) {
        while (true) {
            for (EngineTerminator terminator : engineTerminatorManager.getEngineTerminators()) {
                if (!terminator.getCondition(engineState.getComp())) {
                    comps.add(engineState.getComp());
                    engineState.resetCurrentComp();
                    return;
                }
            }

            Map<Unit, Integer> unitWeights = new HashMap<>();

            for (Unit unit : unitPoolCopy) {
                int weight = engineHeuristicManager.getHeuristic().getWeight(unit);
                unitWeights.put(unit, weight);
            }

            UnitPicker unitPicker = new UnitPicker(engineState, unitWeights, comps);
            Unit nextUnit = unitPicker.getNextUnit();
            engineState.addUnitToComp(nextUnit);
            unitPoolCopy.remove(nextUnit);
        }
    }
}
