package com.tfttools.adapter;

import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;

import java.util.Set;

/**
 * Resolved, validated filter inputs for {@link com.tfttools.service.UnitService#filter}
 */
public record UnitFilterCriteria(Set<Unit> units, Set<Trait> traits) {
}
