package com.tfttools.engine.heuristic.weight;

import com.tfttools.domain.Emblem;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;

import java.util.Optional;

public class TraitsAddedWeightScorer implements EngineWeightScorer
{
    private final EngineState engineState;
    private final Optional<EmblemWeightScorer> emblemScorer;

    // Constructor without emblem scorer
    public TraitsAddedWeightScorer(EngineState engineState) {
        this.engineState = engineState;
        this.emblemScorer = Optional.empty();
    }

    // Constructor with optional emblem scorer
    public TraitsAddedWeightScorer(EngineState engineState, EmblemWeightScorer emblemScorer) {
        this.engineState = engineState;
        this.emblemScorer = Optional.ofNullable(emblemScorer);
    }



    @Override
    public int getWeight(Unit unit)
    {
        int weight = 0;

        // Calculate weight for unit's natural traits
        for (Trait trait : unit.getTraits()) {
            if (!trait.isCountable()) continue;
            weight += calculateTraitWeight(trait);
        }

        // If emblem scorer is available, check for potential emblem traits
        if (emblemScorer.isPresent()) {
            Optional<Trait> potentialEmblemTrait = getBestEmblemTrait(unit, emblemScorer.get());
            if (potentialEmblemTrait.isPresent() && potentialEmblemTrait.get().isCountable()) {
                weight += calculateTraitWeight(potentialEmblemTrait.get());
            }
        }

        return weight;
    }

    private Optional<Trait> getBestEmblemTrait(Unit unit, EmblemWeightScorer emblemScorer) {
        // Access the emblem scorer's logic to find the best emblem for this unit
        return emblemScorer.getBestEmblemForUnit(unit)
                .map(Emblem::getTrait);
    }

    private int calculateTraitWeight(Trait trait) {
        int nextThreshold = getNextThreshold(trait);
        Integer currentThreshold = engineState.getCurrentComp().getTraits().getOrDefault(trait, 0);

        //If the current threshold has reached the max or is past the max, then no more traits you can activate.
        if (currentThreshold > nextThreshold) {
            return 0;
        }

        //If can reach more in the next threshold
        int difference = nextThreshold - currentThreshold;
        int currWeight = nextThreshold - difference + 1;

        //if the next unit to be added increases actives a threshold, then weight should be very desirable.
        if (difference == 1) {
            if (!hasReachedFirstThreshold(trait, currentThreshold)) {
                currWeight *= 5;
            } else {
                currWeight *= 2;
            }
        }

        return currWeight;
    }

    // ... rest of your existing methods
    public int getNextThreshold(Trait trait) {
        int currentThreshold = engineState.getCurrentComp().getTraits().getOrDefault(trait, 0);

        if (currentThreshold == trait.getActivationThresholds()[trait.getActivationThresholds().length - 1])
            return currentThreshold;

        for (int i = trait.getActivationThresholds().length - 1; i > 0; i--) {
            int iteratingThreshold = trait.getActivationThresholds()[i];
            if (currentThreshold >= iteratingThreshold) {
                return trait.getActivationThresholds()[i + 1];
            }
        }

        return trait.getActivationThresholds()[0];
    }

    public boolean hasReachedFirstThreshold(Trait trait, int currentThreshold) {
        return currentThreshold >= trait.getActivationThresholds()[0];
    }

}
