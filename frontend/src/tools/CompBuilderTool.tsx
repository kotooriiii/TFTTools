import React, { useEffect, useMemo, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { UnitData } from '../types/compBuilderTypes';
import { unitService } from '../services/unitService';
import { UnitRoster } from '../components/CompBuilder/UnitRoster.tsx';
import { HexBoard } from '../components/CompBuilder/HexBoard';
import { TraitSynergyPanel } from '../components/CompBuilder/TraitSynergyPanel';

interface CompBuilderNavState {
    seedBoard?: Record<string, UnitData>;
}

const CompBuilderTool: React.FC = () => {
    const location = useLocation();

    const [units, setUnits] = useState<UnitData[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [searchQuery, setSearchQuery] = useState('');
    const [board, setBoard] = useState<Record<string, UnitData>>({});

    useEffect(() => {
        const seedBoard = (location.state as CompBuilderNavState | null)?.seedBoard;
        if (seedBoard) {
            setBoard(seedBoard);
        }
        // location.key is unique per navigation entry (even revisits of this same
        // route), so this reseeds on every "Edit in Comp Builder" handoff.
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [location.key]);

    const [draggedUnit, setDraggedUnit] = useState<UnitData | null>(null);
    const [draggedFromHexId, setDraggedFromHexId] = useState<string | null>(null);
    const [dragOverHexId, setDragOverHexId] = useState<string | null>(null);

    useEffect(() => {
        let cancelled = false;

        (async () => {
            const data = await unitService.getAllUnits();
            if (!cancelled) {
                setUnits(data);
                setIsLoading(false);
            }
        })();

        return () => {
            cancelled = true;
        };
    }, []);

    const placedNames = useMemo(
        () => new Set(Object.values(board).map(unit => unit.apiName)),
        [board]
    );

    const filteredUnits = useMemo(() => {
        const query = searchQuery.trim().toLowerCase();
        if (!query) return units;

        return units.filter(unit =>
            unit.displayName.toLowerCase().includes(query) ||
            unit.traits.some(trait => trait.displayName.toLowerCase().includes(query))
        );
    }, [units, searchQuery]);

    const handleRosterDragStart = (e: React.DragEvent, unit: UnitData) => {
        e.dataTransfer.setData('text/plain', unit.displayName);
        setDraggedUnit(unit);
        setDraggedFromHexId(null);
    };

    const handleHexDragStart = (e: React.DragEvent, id: string, unit: UnitData) => {
        e.dataTransfer.setData('text/plain', unit.displayName);
        setDraggedUnit(unit);
        setDraggedFromHexId(id);
    };

    const handleHexDragOver = (e: React.DragEvent, id: string) => {
        e.preventDefault();
        e.dataTransfer.dropEffect = 'move';
        setDragOverHexId(id);
    };

    const handleHexDragLeave = () => {
        setDragOverHexId(null);
    };

    const handleHexDrop = (e: React.DragEvent, targetHexId: string) => {
        e.preventDefault();

        if (!draggedUnit) return;

        setBoard(prev => {
            const next = { ...prev };

            if (draggedFromHexId) {
                if (draggedFromHexId === targetHexId) return prev;

                const targetOccupant = next[targetHexId];
                next[targetHexId] = draggedUnit;

                if (targetOccupant) {
                    next[draggedFromHexId] = targetOccupant;
                } else {
                    delete next[draggedFromHexId];
                }

                return next;
            }

            // Dropped from the roster: a unit can only occupy one hex at a time
            const existingHexId = Object.keys(prev).find(
                key => prev[key].apiName === draggedUnit.apiName
            );

            if (existingHexId && existingHexId !== targetHexId) {
                delete next[existingHexId];
            }

            next[targetHexId] = draggedUnit;
            return next;
        });

        setDraggedUnit(null);
        setDraggedFromHexId(null);
        setDragOverHexId(null);
    };

    const handleRemove = (id: string) => {
        setBoard(prev => {
            if (!(id in prev)) return prev;
            const next = { ...prev };
            delete next[id];
            return next;
        });
    };

    const handleClearBoard = () => setBoard({});

    return (
        <div className="h-full w-full flex">
            <UnitRoster
                units={filteredUnits}
                isLoading={isLoading}
                searchQuery={searchQuery}
                onSearchChange={setSearchQuery}
                onDragStart={handleRosterDragStart}
                placedNames={placedNames}
            />

            <div className="flex-1 flex flex-col items-center overflow-auto p-6">
                <div className="flex items-center justify-between w-full max-w-3xl mb-4">
                    <div>
                        <h2 className="text-lg font-bold text-primary">Comp Builder</h2>
                        <p className="text-xs text-secondary">
                            Drag units onto the board · drag placed units to rearrange · click to remove
                        </p>
                    </div>
                    <button
                        onClick={handleClearBoard}
                        className="text-xs px-3 py-1.5 rounded-md bg-secondary text-primary hover:opacity-80 transition-opacity"
                    >
                        Clear Board
                    </button>
                </div>

                <HexBoard
                    board={board}
                    dragOverHexId={dragOverHexId}
                    onDrop={handleHexDrop}
                    onDragOver={handleHexDragOver}
                    onDragLeave={handleHexDragLeave}
                    onHexDragStart={handleHexDragStart}
                    onRemove={handleRemove}
                />
            </div>

            <TraitSynergyPanel boardUnits={Object.values(board)} />
        </div>
    );
};

export default CompBuilderTool;
