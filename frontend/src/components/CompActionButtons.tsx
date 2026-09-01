import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { compsService } from '../services/compsService';
import { UnitData } from '../types/compBuilderTypes';
import { parseHexId } from './CompBuilder/hexUtils';
import { Button } from './Button';

type CopyStatus = 'idle' | 'copying' | 'copied' | 'error';
type SaveStatus = 'idle' | 'saving' | 'saved' | 'error';

interface CompActionButtonsProps {
    board: Record<string, UnitData>;
    /** Resolves the team code to copy - a precomputed one (Horizontal Comp Generator) or one fetched on demand (My Comps). */
    getTeamCode: () => Promise<string>;
    /** Renders a Delete button when provided (My Comps only - a generated composition isn't a saved entity to delete). */
    onDelete?: () => void;
}

const boardToPlacements = (board: Record<string, UnitData>) =>
    Object.entries(board).map(([id, unit]) => {
        const { row, col } = parseHexId(id);
        return { unitApiName: unit.apiName, row, col };
    });

/**
 * The Copy Team Code / Edit in Comp Builder / Save Comp (/ Delete) action row shown under a
 * comp's hex board - shared between HorizontalCompositionGenerator's result cards and My Comps'
 * expanded rows so the behavior and styling only live in one place.
 */
export const CompActionButtons: React.FC<CompActionButtonsProps> = ({ board, getTeamCode, onDelete }) => {
    const navigate = useNavigate();
    const { user, token } = useAuth();

    const [copyStatus, setCopyStatus] = useState<CopyStatus>('idle');
    const [saveStatus, setSaveStatus] = useState<SaveStatus>('idle');

    const handleCopyTeamCode = async () => {
        setCopyStatus('copying');
        try {
            const teamCode = await getTeamCode();
            await navigator.clipboard.writeText(teamCode);
            setCopyStatus('copied');
        } catch (err) {
            console.error('Error generating team code:', err);
            setCopyStatus('error');
        }
    };

    const handleSaveComp = async () => {
        if (!token || Object.keys(board).length === 0) return;

        setSaveStatus('saving');
        try {
            await compsService.saveComp(token, boardToPlacements(board));
            setSaveStatus('saved');
        } catch (err) {
            console.error('Error saving comp:', err);
            setSaveStatus('error');
        }
    };

    return (
        <div className="flex flex-col items-center gap-2">
            <div className="flex items-center justify-center gap-2">
                <Button
                    onClick={handleCopyTeamCode}
                    disabled={copyStatus === 'copying'}
                    className="px-3 py-2 rounded-md text-sm font-medium"
                >
                    {copyStatus === 'copying' ? 'Generating...' : copyStatus === 'copied' ? 'Copied!' : copyStatus === 'error' ? 'Failed - Retry' : 'Copy Team Code'}
                </Button>
                <Button
                    tone="accent"
                    onClick={() => navigate('/tools/comp-builder', { state: { seedBoard: board } })}
                    className="px-3 py-2 rounded-md text-sm font-medium"
                >
                    Edit in Comp Builder
                </Button>
                <Button
                    onClick={handleSaveComp}
                    disabled={!user || saveStatus === 'saving' || saveStatus === 'saved'}
                    title={!user ? 'Log in to save comps' : undefined}
                    className="px-3 py-2 rounded-md text-sm font-medium"
                >
                    {saveStatus === 'saving' ? 'Saving...' : saveStatus === 'saved' ? 'Saved' : saveStatus === 'error' ? 'Retry Save' : !user ? 'Log in to Save' : 'Save Comp'}
                </Button>
                {onDelete && (
                    <Button
                        tone="danger"
                        onClick={onDelete}
                        className="px-3 py-2 rounded-md text-sm font-medium"
                    >
                        Delete
                    </Button>
                )}
            </div>
            {saveStatus === 'error' && (
                <div className="text-center text-xs text-red-600">Failed to save comp. Please try again.</div>
            )}
        </div>
    );
};
