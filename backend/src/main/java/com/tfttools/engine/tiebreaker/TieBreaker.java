package com.tfttools.engine.tiebreaker;

import com.tfttools.domain.Unit;

import java.util.List;

public interface TieBreaker {
    public Unit chooseUnit(List<Unit> candidates);
}
