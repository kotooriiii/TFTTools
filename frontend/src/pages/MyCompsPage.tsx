import React, { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { compsService, CompResponse } from '../services/compsService';
import { unitService } from '../services/unitService';
import { UnitData } from '../types/compBuilderTypes';
import { HexBoard } from '../components/CompBuilder/HexBoard';
import { UnitPortrait } from '../components/UnitPortrait.tsx';
import { hexId } from '../components/CompBuilder/hexUtils';
import { computeTraitSummary } from '../utils/traitSummary';
import { getCostColor } from '../utils/unitDisplay';

/**
 * Resolves a saved comp's placements (identity + hex position only) into rehydrated
 * UnitData, dropping any placement whose unit no longer resolves (matching the seedBoard
 * rehydration pattern used by CompBuilderTool/HorizontalCompositionGenerator). A unit's
 * display-name/icon/cost changing since the comp was saved doesn't affect this - only
 * apiName identity is stored, so the comp still renders correctly.
 */
const rehydrateUnits = (comp: CompResponse, unitsByApiName: Map<string, UnitData>): UnitData[] =>
    comp.placements
        .map(placement => unitsByApiName.get(placement.unitApiName))
        .filter((unit): unit is UnitData => unit !== undefined);

const rehydrateBoard = (comp: CompResponse, unitsByApiName: Map<string, UnitData>): Record<string, UnitData> => {
    const board: Record<string, UnitData> = {};
    comp.placements.forEach(placement => {
        const unit = unitsByApiName.get(placement.unitApiName);
        if (unit) {
            board[hexId(placement.row, placement.col)] = unit;
        }
    });
    return board;
};

const formatSaveDate = (createdAt: string): string => {
    try {
        return new Date(createdAt).toLocaleString();
    } catch {
        return createdAt;
    }
};

interface CompRowProps {
    comp: CompResponse;
    units: UnitData[];
    board: Record<string, UnitData>;
    onDelete: (id: string) => void;
}

const CompRow: React.FC<CompRowProps> = ({ comp, units, board, onDelete }) => {
    const [expanded, setExpanded] = useState(false);
    const [copyStatus, setCopyStatus] = useState<'idle' | 'copying' | 'copied' | 'error'>('idle');

    const traitSummary = useMemo(() => computeTraitSummary(units), [units]);
    const activeTraitsText = traitSummary
        .filter(trait => trait.active)
        .map(trait => `${trait.count} ${trait.displayName}`)
        .join(', ');

    const handleCopyTeamCode = async () => {
        setCopyStatus('copying');
        try {
            const teamCode = await compsService.getTeamCode(comp.placements.map(p => p.unitApiName));
            await navigator.clipboard.writeText(teamCode);
            setCopyStatus('copied');
        } catch (err) {
            console.error('Error generating team code:', err);
            setCopyStatus('error');
        }
    };

    return (
        <div className="border border-border rounded-lg bg-card overflow-hidden">
            <div
                className="flex items-center gap-4 p-4 cursor-pointer hover:bg-accent/30 transition-colors"
                onClick={() => setExpanded(prev => !prev)}
            >
                <div className="flex -space-x-2 shrink-0">
                    {units.map((unit, i) => (
                        <UnitPortrait
                            key={`${unit.apiName}-${i}`}
                            displayName={unit.displayName}
                            iconUrl={unit.iconUrl}
                            size={36}
                            borderColor={getCostColor(unit.cost)}
                        />
                    ))}
                    {units.length === 0 && (
                        <span className="text-xs text-secondary italic">No units could be rehydrated</span>
                    )}
                </div>

                <div className="flex-1 min-w-0">
                    <div className="text-sm text-primary truncate">
                        {activeTraitsText || 'No active traits'}
                    </div>
                    <div className="text-xs text-secondary">
                        Saved {formatSaveDate(comp.createdAt)}
                    </div>
                </div>

                <button
                    onClick={(e) => {
                        e.stopPropagation();
                        onDelete(comp.id);
                    }}
                    aria-label="Delete comp"
                    title="Delete comp"
                    className="shrink-0 w-7 h-7 flex items-center justify-center rounded-full text-secondary hover:bg-red-100 hover:text-red-700 transition-colors text-lg leading-none"
                >
                    ×
                </button>

                <span className="text-secondary shrink-0">{expanded ? '▲' : '▼'}</span>
            </div>

            {expanded && (
                <div className="border-t border-border p-4 flex flex-col items-center gap-4">
                    <div className="flex justify-center gap-4">
                        <HexBoard board={board} readOnly />

                        {traitSummary.length > 0 && (
                            <div className="w-56 shrink-0 border-l border-border bg-primary p-3 rounded-r-lg">
                                <h5 className="text-sm font-bold text-primary mb-3">Active Traits</h5>
                                <div className="flex flex-col gap-2">
                                    {traitSummary.map((trait) => (
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
                        )}
                    </div>

                    <div className="flex items-center gap-2">
                        <button
                            onClick={handleCopyTeamCode}
                            disabled={copyStatus === 'copying'}
                            className="px-3 py-2 bg-secondary text-primary rounded-md text-sm font-medium hover:bg-secondary/90 disabled:opacity-50 transition-colors duration-200"
                        >
                            {copyStatus === 'copying' ? 'Generating...' : copyStatus === 'copied' ? 'Copied!' : copyStatus === 'error' ? 'Failed - Retry' : 'Copy Team Code'}
                        </button>
                        <button
                            onClick={() => onDelete(comp.id)}
                            className="px-3 py-2 bg-red-100 text-red-700 rounded-md text-sm font-medium hover:bg-red-200 transition-colors duration-200"
                        >
                            Delete
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export const MyCompsPage: React.FC = () => {
    const { user, token, isLoading } = useAuth();

    const [comps, setComps] = useState<CompResponse[]>([]);
    const [unitsByApiName, setUnitsByApiName] = useState<Map<string, UnitData>>(new Map());
    const [isLoadingComps, setIsLoadingComps] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!user || !token) return;

        let cancelled = false;

        (async () => {
            setIsLoadingComps(true);
            setError(null);
            try {
                const [allUnits, userComps] = await Promise.all([
                    unitService.getAllUnits(),
                    compsService.listComps(token)
                ]);

                if (cancelled) return;

                setUnitsByApiName(new Map(allUnits.map(unit => [unit.apiName, unit])));
                setComps(userComps);
            } catch (err) {
                console.error('Error loading My Comps:', err);
                if (!cancelled) {
                    setError('Failed to load your saved comps. Please try again.');
                }
            } finally {
                if (!cancelled) {
                    setIsLoadingComps(false);
                }
            }
        })();

        return () => {
            cancelled = true;
        };
    }, [user, token]);

    const handleDelete = async (id: string) => {
        if (!token) return;

        try {
            await compsService.deleteComp(token, id);
            setComps(prev => prev.filter(comp => comp.id !== id));
        } catch (err) {
            console.error('Error deleting comp:', err);
            setError('Failed to delete comp. Please try again.');
        }
    };

    if (isLoading) {
        return null;
    }

    if (!user) {
        return (
            <div className="flex justify-center items-start pt-24 min-h-screen">
                <div className="text-center">
                    <p className="text-secondary mb-4">You need to be logged in to view your comps.</p>
                    <Link to="/login" className="text-accent font-medium">Log in</Link>
                </div>
            </div>
        );
    }

    return (
        <div className="p-8 max-w-5xl mx-auto min-h-screen">
            <h1 className="text-3xl font-bold text-primary mb-4">My Comps</h1>

            {error && (
                <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
                    {error}
                </div>
            )}

            {isLoadingComps ? (
                <p className="text-secondary">Loading your saved comps...</p>
            ) : comps.length === 0 ? (
                <p className="text-secondary">
                    You haven't saved any comps yet. Save one from the Horizontal Comp Generator or Comp Builder.
                </p>
            ) : (
                <div className="flex flex-col gap-3">
                    {comps.map(comp => (
                        <CompRow
                            key={comp.id}
                            comp={comp}
                            units={rehydrateUnits(comp, unitsByApiName)}
                            board={rehydrateBoard(comp, unitsByApiName)}
                            onDelete={handleDelete}
                        />
                    ))}
                </div>
            )}
        </div>
    );
};
