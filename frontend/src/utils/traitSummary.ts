// Shared trait-count-from-units logic, previously duplicated between
// TraitSynergyPanel.tsx and HorizontalCompositionGenerator.tsx's getTraitBreakdown - a third
// near-duplicate would otherwise be needed for My Comps.

export interface TraitLike {
    apiName: string;
    displayName: string;
    activationThresholds: number[];
    count: number;
}

export interface UnitLike {
    traits: TraitLike[];
}

export interface TraitSummary {
    apiName: string;
    displayName: string;
    count: number;
    active: boolean;
    activeThreshold: number | null;
    nextThreshold: number | null;
}

/**
 * Sums each trait's count across the given units and figures out, per trait, whether it's
 * currently active (count meets or exceeds some activation threshold) and what the next
 * threshold to reach is. Sorted active-first, then by descending count.
 */
export const computeTraitSummary = (units: UnitLike[]): TraitSummary[] => {
    const counts = new Map<string, { displayName: string; count: number; thresholds: number[] }>();

    units.forEach(unit => {
        unit.traits.forEach(trait => {
            const existing = counts.get(trait.apiName);
            if (existing) {
                existing.count += trait.count;
            } else {
                counts.set(trait.apiName, {
                    displayName: trait.displayName,
                    count: trait.count,
                    thresholds: trait.activationThresholds
                });
            }
        });
    });

    return [...counts.entries()]
        .map(([apiName, { displayName, count, thresholds }]) => {
            const sorted = [...thresholds].sort((a, b) => a - b);
            const activeThreshold = [...sorted].reverse().find(t => count >= t) ?? null;
            const nextThreshold = sorted.find(t => t > count) ?? null;
            return {
                apiName,
                displayName,
                count,
                active: activeThreshold !== null,
                activeThreshold,
                nextThreshold
            };
        })
        .sort((a, b) => {
            if (a.active !== b.active) return a.active ? -1 : 1;
            return b.count - a.count;
        });
};
