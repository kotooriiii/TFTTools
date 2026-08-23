import React, { useMemo } from 'react';
import { ChampionData } from '../../types/compBuilderTypes';

interface TraitSynergyPanelProps {
    boardChampions: ChampionData[];
}

interface TraitSummary {
    displayName: string;
    count: number;
    activeThreshold: number | null;
    nextThreshold: number | null;
}

export const TraitSynergyPanel: React.FC<TraitSynergyPanelProps> = ({ boardChampions }) => {
    const traitSummaries = useMemo<TraitSummary[]>(() => {
        const counts = new Map<string, { count: number; thresholds: number[] }>();

        boardChampions.forEach(champion => {
            champion.traits.forEach(trait => {
                const existing = counts.get(trait.displayName);
                if (existing) {
                    existing.count += 1;
                } else {
                    counts.set(trait.displayName, { count: 1, thresholds: trait.activationThresholds });
                }
            });
        });

        return [...counts.entries()]
            .map(([displayName, { count, thresholds }]) => {
                const sorted = [...thresholds].sort((a, b) => a - b);
                const activeThreshold = [...sorted].reverse().find(t => count >= t) ?? null;
                const nextThreshold = sorted.find(t => t > count) ?? null;
                return { displayName, count, activeThreshold, nextThreshold };
            })
            .sort((a, b) => {
                if (!!a.activeThreshold !== !!b.activeThreshold) return a.activeThreshold ? -1 : 1;
                return b.count - a.count;
            });
    }, [boardChampions]);

    return (
        <div className="w-64 shrink-0 h-full border-l border-border bg-primary p-3 overflow-y-auto custom-scrollbar">
            <h3 className="text-sm font-bold text-primary mb-3">Active Traits</h3>

            {traitSummaries.length === 0 && (
                <div className="text-secondary text-xs italic">
                    Drag champions to the board to see synergies
                </div>
            )}

            <div className="flex flex-col gap-2">
                {traitSummaries.map(trait => (
                    <div
                        key={trait.displayName}
                        className="rounded-lg px-3 py-2 flex items-center justify-between"
                        style={{
                            backgroundColor: trait.activeThreshold ? 'rgba(76,175,80,0.15)' : 'rgba(0,0,0,0.04)',
                            border: `1px solid ${trait.activeThreshold ? '#4CAF50' : 'transparent'}`
                        }}
                    >
                        <span className={`text-xs font-semibold ${trait.activeThreshold ? 'text-primary' : 'text-secondary'}`}>
                            {trait.displayName}
                        </span>
                        <span className="text-xs font-mono text-secondary">
                            {trait.count}{trait.nextThreshold ? ` / ${trait.nextThreshold}` : ''}
                        </span>
                    </div>
                ))}
            </div>
        </div>
    );
};
