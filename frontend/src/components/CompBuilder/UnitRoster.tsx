import React, { useMemo } from 'react';
import { motion } from 'framer-motion';
import { UnitData } from '../../types/compBuilderTypes';
import { getCostColor } from '../../utils/unitDisplay';
import { UnitPortrait } from '../UnitPortrait.tsx';

interface UnitRosterProps {
    units: UnitData[];
    isLoading: boolean;
    searchQuery: string;
    onSearchChange: (query: string) => void;
    onDragStart: (e: React.DragEvent, unit: UnitData) => void;
    placedNames: Set<string>;
}

export const UnitRoster: React.FC<UnitRosterProps> = ({
                                                                    units,
                                                                    isLoading,
                                                                    searchQuery,
                                                                    onSearchChange,
                                                                    onDragStart,
                                                                    placedNames
                                                                }) => {
    const groupedByCost = useMemo(() => {
        const map = new Map<number, UnitData[]>();

        [...units]
            .sort((a, b) => a.displayName.localeCompare(b.displayName))
            .forEach(unit => {
                if (!map.has(unit.cost)) map.set(unit.cost, []);
                map.get(unit.cost)!.push(unit);
            });

        return [...map.entries()].sort(([costA], [costB]) => costA - costB);
    }, [units]);

    return (
        <div className="w-72 shrink-0 h-full flex flex-col border-r border-border bg-primary">
            <div className="p-3 border-b border-border">
                <input
                    type="text"
                    value={searchQuery}
                    onChange={(e) => onSearchChange(e.target.value)}
                    placeholder="Search units or traits..."
                    className="w-full px-3 py-2 rounded-lg border border-border bg-primary text-sm text-primary outline-none focus:ring-1 focus:ring-border"
                />
            </div>

            <div className="flex-1 overflow-y-auto p-3 custom-scrollbar">
                {isLoading && (
                    <div className="text-secondary text-sm text-center py-8">Loading units…</div>
                )}

                {!isLoading && groupedByCost.length === 0 && (
                    <div className="text-secondary text-sm text-center py-8">No units found</div>
                )}

                {!isLoading && groupedByCost.map(([cost, group]) => (
                    <div key={cost} className="mb-4">
                        <div className="text-xs font-bold mb-2 flex items-center gap-2">
                            <span
                                className="w-3 h-3 rounded-full inline-block"
                                style={{ backgroundColor: getCostColor(cost) }}
                            />
                            <span className="text-secondary">{cost} Cost</span>
                        </div>
                        <div className="grid grid-cols-3 gap-2">
                            {group.map(unit => {
                                const isPlaced = placedNames.has(unit.displayName);
                                return (
                                    <motion.div
                                        key={unit.displayName}
                                        draggable
                                        onDragStart={(e) => onDragStart(e, unit)}
                                        whileHover={{ scale: 1.05 }}
                                        whileTap={{ scale: 0.95 }}
                                        title={unit.traits.map(t => t.displayName).join(', ')}
                                        className="rounded-lg p-1.5 flex flex-col items-center gap-1 cursor-grab select-none bg-white"
                                        style={{
                                            border: `2px solid ${getCostColor(unit.cost)}`,
                                            opacity: isPlaced ? 0.45 : 1
                                        }}
                                    >
                                        <UnitPortrait
                                            displayName={unit.displayName}
                                            iconUrl={unit.iconUrl}
                                            size={36}
                                        />
                                        <div className="text-[10px] text-center leading-tight text-primary truncate w-full">
                                            {unit.displayName}
                                        </div>
                                    </motion.div>
                                );
                            })}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};
