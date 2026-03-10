package com.tfttools.engine.tiebreaker;

import com.tfttools.domain.Unit;

import java.util.*;

public class DiversityTieBreaker implements TieBreaker {
    private final List<Set<Unit>> previousComps;

    public DiversityTieBreaker(List<Set<Unit>> previousComps) {
        this.previousComps = previousComps;
    }

    @Override
    public Unit chooseUnit(List<Unit> ties) {
        List<Unit> candidates = new ArrayList<>();

        for (Unit unit : ties) {
            boolean flag = true;
            for (Set<Unit> comp : previousComps) {
                if (!comp.contains(unit) && flag) {
                    continue;
                } else {
                    flag = false;
                }
            }
            if (flag) candidates.add(unit);
        }

        if (!candidates.isEmpty()) {
            return candidates.stream().toList().get(0);
        } else {
            return ties.get(0);
        }
    }
}
