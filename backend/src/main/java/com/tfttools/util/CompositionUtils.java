package com.tfttools.util;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//todo singleton
public class CompositionUtils
{

    public void reduceCompositionToSynergies(Composition composition)
    {
        Map<Trait, Integer> traits = composition.getTraits();
        traits.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .forEach(entry -> composition.remove(entry.getKey()));
    }

    /**
     * Checks if adding the specified unit to the {@code composition} would activate any trait's first threshold.
     * This is useful for determining if a unit would provide meaningful synergy activation.
     * This does NOT modify the {@code composition} - it only performs a hypothetical check.
     *
     * @param composition The current team composition
     * @param unit        The unit being considered for addition
     * @return true if adding the unit would activate at least one trait's first threshold
     */

    public static boolean wouldUnitActivateAnyTraitFirstThreshold(Composition composition, Unit unit)
    {
        return !getTraitsReachingFirstThresholdWhenUnitAdded(composition, unit).isEmpty();
    }

    /**
     * Returns all traits from the unit that would reach their first activation threshold
     * when the unit is added to the {@code composition}. Only considers traits that are not
     * already at their first threshold and would be activated by adding exactly one more unit.
     * This does NOT modify the {@code composition} - it only performs a hypothetical analysis.
     *
     * @param composition The current team composition
     * @param unit        The unit being considered for addition
     * @return A list of traits that would reach their first threshold when this unit is added
     */

    public static List<Trait> getTraitsReachingFirstThresholdWhenUnitAdded(Composition composition, Unit unit)
    {
        Map<Trait, Integer> compTraits = composition.getTraits();
        List<Trait> unitTraits = unit.getTraits();

        List<Trait> activatableTraits = new ArrayList<>();
        for (Trait unitTrait : unitTraits)
        {
            if (!unitTrait.isCountable())
                continue;

            // If the current comp does not include this trait, it should return 0
            int currentCompTraitCount = compTraits.getOrDefault(unitTrait, 0);

            // We don't really care about thresholds that are already met
            if (hasReachedFirstThreshold(unitTrait, currentCompTraitCount))
                continue;
            if (!willActivateNextThreshold(unitTrait, currentCompTraitCount))
                continue;
            activatableTraits.add(unitTrait);
        }
        return activatableTraits;
    }


    /**
     * Checks if adding a unit with the given trait will activate the next threshold
     *
     * @param trait             The trait to check
     * @param currentTraitCount Current count of this trait in the composition
     * @return true if adding one more unit with this trait will activate the next threshold
     */
    public static boolean willActivateNextThreshold(Trait trait, int currentTraitCount)
    {
        int nextThreshold = getNextThreshold(trait, currentTraitCount);

        // If we're already at or past max threshold, can't activate anything new
        if (currentTraitCount >= nextThreshold)
        {
            return false;
        }

        int difference = nextThreshold - currentTraitCount;

        // Check if adding 1 more will reach the next threshold, some units may count as 2 unit space // 2 trait space TODO
        return difference == 1;

    }

    /**
     * Gets the next threshold for a trait given current count
     */
    public static int getNextThreshold(Trait trait, int currentTraitCount)
    {
        int[] thresholds = trait.getActivationThresholds();

        // If already at max threshold, return -1
        if (currentTraitCount >= thresholds[thresholds.length - 1])
        {
            return -1;
        }

        // Find the next threshold above current count
        for (int threshold : thresholds)
        {
            if (currentTraitCount < threshold)
            {
                return threshold;
            }
        }

        return thresholds[0]; // Fallback to first threshold
    }

    public static int getPreviousThreshold(Trait trait, int currentTraitCount)
    {
        int[] thresholds = trait.getActivationThresholds();

        if(currentTraitCount <= thresholds[0])
            return 0;

        for (int i = 1; i < thresholds.length; i++)
        {
            if (currentTraitCount <= thresholds[i])
            {
                return thresholds[i - 1];
            }
        }

        return thresholds[thresholds.length - 1]; //todo this or - 2
    }

    public static boolean hasReachedFirstThreshold(Trait trait, int currentThreshold)
    {
        return currentThreshold >= trait.getActivationThresholds()[0];
    }

    public static List<Trait> getActivatedTraits(Composition composition)
    {
        return getActivatedTraits(composition, false);
    }

    public static List<Trait> getActivatedTraits(Composition composition, boolean shouldCountUniqueTraits)
    {
        Map<Trait, Integer> traits = composition.getTraits();
        return traits.keySet().stream().filter(trait -> shouldCountUniqueTraits || trait.isCountable()).filter(trait -> hasReachedFirstThreshold(trait, traits.get(trait))).toList();
    }
}
