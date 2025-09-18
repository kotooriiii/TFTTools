package com.tfttools.engine;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.registry.UnitRegistry;

import java.util.*;

public class EngineState {
    private final Set<Unit> currentComp;
    private final List<Trait> availableEmblems;
    private final Map<Trait, Integer> currentTraits;
    private final int tactitionLevel;
    private final Set<Unit> core;
    private final UnitRegistry unitRegistry;

    public EngineState(Set<Unit> comp, List<Unit> requiredUnits, List<Trait> availableEmblems, int tactitionLevel, UnitRegistry unitRegistry) {
        this.currentComp = comp;
        this.availableEmblems = availableEmblems;
        this.currentTraits = new HashMap<>();
        this.tactitionLevel = tactitionLevel;
        this.unitRegistry = unitRegistry;
        this.core = new HashSet<>(requiredUnits);

        initTraits();
        addCoreToComp();
    }

    private void addCoreToComp() {
        this.core.forEach(this::addUnitToComp);
    }

    private void initTraits(){
        for (Trait trait : unitRegistry.getAllTraits()) {
            currentTraits.put(trait, 0);
        }

        for (Unit unit : core) {
            for (Trait trait : unit.getTraits()) {
                currentTraits.put(trait, currentTraits.get(trait) + 1);
            }
        }
    }

    public int getTactitionLevel() {
        return tactitionLevel;
    }

    public Map<Trait, Integer> getCurrentTraits() {
        return currentTraits;
    }

    public Set<Unit> getComp() {
        return new HashSet<>(currentComp);
    }

    public void addUnitToComp(Unit unit) {
        currentComp.add(unit);

        for (Trait trait : unit.getTraits()) {
            currentTraits.put(trait, currentTraits.get(trait) + 1);
        }
    }

    public void resetCurrentComp() {
        this.currentComp.retainAll(this.core);
        this.currentTraits.clear();

        initTraits();
    }

    public Set<Unit> getCore() {
        return core;
    }

    public boolean addToCore(Unit unit) {
        return core.add(unit);
    }
}
