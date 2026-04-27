package com.tfttools.engine.heuristic.weight;

import com.tfttools.domain.Emblem;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;
import com.tfttools.util.CompositionUtils;

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
        Integer currentTraitCount = engineState.getCurrentComp().getTraits().getOrDefault(trait, 0);
        int nextThreshold = CompositionUtils.INSTANCE.getNextThreshold(trait, currentTraitCount);

        //If the current threshold has reached the max or is past the max, then no more traits you can activate.
        if (currentTraitCount > nextThreshold) {
            return 0;
        }


        int currWeight = 0;

        //if the next unit to be added increases actives a threshold, then weight should be very desirable.
        if (CompositionUtils.INSTANCE.willActivateNextThreshold(trait, currentTraitCount)) {
            if (!CompositionUtils.INSTANCE.hasReachedFirstThreshold(trait, currentTraitCount)) {
                currWeight += 3;
            } else {
                currWeight += 1;
            }
        }
        else //does not activate but progresses
        {

            if (!CompositionUtils.INSTANCE.hasReachedFirstThreshold(trait, currentTraitCount)) {

                if(currentTraitCount == 0)
                {
                    // did not activate, has not reached first threshold,  new trait
                }
                else
                {
                    //did not activate, has not reached first threshold,  progressing
                }

                int requiredTurns = nextThreshold - currentTraitCount;
                int remainingTurns = engineState.getEngineConfiguration().getMaxUnitsOnBoard() - engineState.getCurrentComp().getUnits().size();
                //if te
                if(remainingTurns >= requiredTurns)
                {
                    int difference = currentTraitCount - CompositionUtils.INSTANCE.getPreviousThreshold(trait, currentTraitCount) + 1;
                    currWeight += difference;
                }
            } else {
                // does not activate, already reached first threshold, doesnot matter
                currWeight += 0;
            }
        }

        return currWeight;
    }



}
