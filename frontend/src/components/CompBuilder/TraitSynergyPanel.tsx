import React, { useMemo } from 'react';
import { UnitData } from '../../types/compBuilderTypes';
import { computeTraitSummary } from '../../utils/traitSummary';

interface TraitSynergyPanelProps {
    boardUnits: UnitData[];
}

export const TraitSynergyPanel: React.FC<TraitSynergyPanelProps> = ({ boardUnits }) => {
    const traitSummaries = useMemo(() => computeTraitSummary(boardUnits), [boardUnits]);

    return (
        <div className="w-64 shrink-0 h-full border-l border-border bg-primary p-3 overflow-y-auto custom-scrollbar">
            <h3 className="text-sm font-bold text-primary mb-3">Active Traits</h3>

            {traitSummaries.length === 0 && (
                <div className="text-secondary text-xs italic">
                    Drag units to the board to see synergies
                </div>
            )}

            <div className="flex flex-col gap-2">
                {traitSummaries.map(trait => (
                    <div
                        key={trait.apiName}
                        className="rounded-lg px-3 py-2 flex items-center justify-between"
                        style={{
                            backgroundColor: trait.active ? 'rgba(76,175,80,0.15)' : 'rgba(0,0,0,0.04)',
                            border: `1px solid ${trait.active ? '#4CAF50' : 'transparent'}`
                        }}
                    >
                        <span className={`text-xs font-semibold ${trait.active ? 'text-primary' : 'text-secondary'}`}>
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
