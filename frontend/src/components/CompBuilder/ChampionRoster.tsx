import React, { useMemo } from 'react';
import { motion } from 'framer-motion';
import { ChampionData } from '../../types/compBuilderTypes';
import { getCostColor, getInitials } from './hexUtils';

interface ChampionRosterProps {
    champions: ChampionData[];
    isLoading: boolean;
    searchQuery: string;
    onSearchChange: (query: string) => void;
    onDragStart: (e: React.DragEvent, champion: ChampionData) => void;
    placedNames: Set<string>;
}

export const ChampionRoster: React.FC<ChampionRosterProps> = ({
                                                                    champions,
                                                                    isLoading,
                                                                    searchQuery,
                                                                    onSearchChange,
                                                                    onDragStart,
                                                                    placedNames
                                                                }) => {
    const groupedByCost = useMemo(() => {
        const map = new Map<number, ChampionData[]>();

        [...champions]
            .sort((a, b) => a.displayName.localeCompare(b.displayName))
            .forEach(champion => {
                if (!map.has(champion.cost)) map.set(champion.cost, []);
                map.get(champion.cost)!.push(champion);
            });

        return [...map.entries()].sort(([costA], [costB]) => costA - costB);
    }, [champions]);

    return (
        <div className="w-72 shrink-0 h-full flex flex-col border-r border-border bg-primary">
            <div className="p-3 border-b border-border">
                <input
                    type="text"
                    value={searchQuery}
                    onChange={(e) => onSearchChange(e.target.value)}
                    placeholder="Search champions or traits..."
                    className="w-full px-3 py-2 rounded-lg border border-border bg-primary text-sm text-primary outline-none focus:ring-1 focus:ring-border"
                />
            </div>

            <div className="flex-1 overflow-y-auto p-3 custom-scrollbar">
                {isLoading && (
                    <div className="text-secondary text-sm text-center py-8">Loading champions…</div>
                )}

                {!isLoading && groupedByCost.length === 0 && (
                    <div className="text-secondary text-sm text-center py-8">No champions found</div>
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
                            {group.map(champion => {
                                const isPlaced = placedNames.has(champion.displayName);
                                return (
                                    <motion.div
                                        key={champion.displayName}
                                        draggable
                                        onDragStart={(e) => onDragStart(e, champion)}
                                        whileHover={{ scale: 1.05 }}
                                        whileTap={{ scale: 0.95 }}
                                        title={champion.traits.map(t => t.displayName).join(', ')}
                                        className="rounded-lg p-1.5 flex flex-col items-center gap-1 cursor-grab select-none bg-white"
                                        style={{
                                            border: `2px solid ${getCostColor(champion.cost)}`,
                                            opacity: isPlaced ? 0.45 : 1
                                        }}
                                    >
                                        <div
                                            className="w-9 h-9 rounded-full flex items-center justify-center text-white text-[10px] font-bold"
                                            style={{ backgroundColor: '#3B3B1A' }}
                                        >
                                            {getInitials(champion.displayName)}
                                        </div>
                                        <div className="text-[10px] text-center leading-tight text-primary truncate w-full">
                                            {champion.displayName}
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
