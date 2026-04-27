package com.tfttools.engine.heuristic;

import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.engine.heuristic.tiebreaker.DiversityTieBreaker;
import com.tfttools.engine.heuristic.weight.*;
import com.tfttools.repository.UnitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WeightRegistry {
    private final EngineConfiguration configuration;
    private final UnitRepository unitRepository;

    public WeightRegistry(EngineConfiguration configuration, UnitRepository unitRepository) {
        this.configuration = configuration;
        this.unitRepository = unitRepository;
    }

    public SynergyLookaheadWeightScorer createSynergyLookahead(EngineState engineState) {
        return new SynergyLookaheadWeightScorer(engineState, unitRepository); // Pass it here
    }

    public EmblemWeightScorer createEmblemWeightScorer(EngineState engineState)
    {
        return new EmblemWeightScorer(engineState, new ArrayList<>(configuration.getEmblems()));
    }

    // When you want traits scorer to consider emblems
    public TraitsAddedWeightScorer createTraitsAddedWeightWithEmblems(
            EngineState engineState,
            EmblemWeightScorer emblemScorer) {
        return new TraitsAddedWeightScorer(engineState, emblemScorer);
    }

    // When you want traits scorer to work independently
    public TraitsAddedWeightScorer createTraitsAddedWeight(EngineState engineState) {
        return new TraitsAddedWeightScorer(engineState);
    }


    public RequiredTraitsWeightScorer createRequiredTraitsWeight(EngineState engineState) {
        return new RequiredTraitsWeightScorer(engineState, configuration.getRequiredTraits());
    }

    public LuckWeightScorer createLuckWeight(EngineState engineState) {
        return new LuckWeightScorer(engineState, configuration.getLuck());
    }

    public CostWeightScorer createCostWeight() {
        return new CostWeightScorer();
    }

    public RequiredUnitsWeightScorer createRequiredUnitsWeight(EngineState engineState)
    {
        return new RequiredUnitsWeightScorer(engineState);
    }
    // Tiebreaker factory methods
    public DiversityTieBreaker createDiversityTieBreaker(List<Composition> previousComps) {
        return new DiversityTieBreaker(previousComps);
    }

    // Builder factory method
    public HeuristicBuilder builder() {
        return new HeuristicBuilder();
    }
}