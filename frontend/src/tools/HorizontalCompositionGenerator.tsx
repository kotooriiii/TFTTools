import React, {useRef, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {AnimatePresence, motion} from 'framer-motion';
import {EmblemItem, SelectedItem} from '../types/searchTypes';
import {CompositionDTO, generateHorizontalComposition, HorizontalDTO, UnitPlacementDTO} from '../services/searchService';
import {compsService} from '../services/compsService';
import {useAuth} from '../contexts/AuthContext';

import {EmblemSearchBox, EmblemSearchBoxHandle} from "../components/HorizontalCompositionGenerator/EmblemSearchBox.tsx";
import {TraitSearchBox, TraitSearchBoxHandle} from '../components/HorizontalCompositionGenerator/TraitSearchBox.tsx';
import {
    UnitSearchBox,
    UnitSearchBoxHandle
} from "../components/HorizontalCompositionGenerator/UnitSearchBox.tsx";
import {HexBoard} from '../components/CompBuilder/HexBoard';
import {hexId} from '../components/CompBuilder/hexUtils';
import {UnitData} from '../types/compBuilderTypes';
import {computeTraitSummary} from '../utils/traitSummary';

interface BasicInputs
{
    tacticianLevel: number;
    requiredUnits: SelectedItem[];
    requiredTraits: { trait: string; count: number }[];
}

interface AdvancedInputs
{
    crownsPans: number;
    luck: number;
    targetGold: number;
    emblems: EmblemItem[];
}

interface TFTCompositionResult
{
    compositions: CompositionDTO[];
}

type SaveStatus = 'idle' | 'saving' | 'saved' | 'error';

const placementsToBoard = (placements: UnitPlacementDTO[]): Record<string, UnitData> =>
{
    const board: Record<string, UnitData> = {};
    placements.forEach(placement =>
    {
        board[hexId(placement.row, placement.col)] = {
            apiName: placement.unit.apiName,
            displayName: placement.unit.displayName ?? '',
            cost: placement.unit.cost ?? 0,
            traits: (placement.unit.traits ?? []).map(trait => ({
                apiName: trait.apiName,
                displayName: trait.displayName ?? '',
                activationThresholds: trait.activationThresholds,
                count: trait.count ?? 1
            })),
            iconUrl: placement.unit.iconUrl
        };
    });
    return board;
};

const HorizontalCompositionGenerator: React.FC = () =>
{
    const navigate = useNavigate();
    const {user, token} = useAuth();

    const [saveStatus, setSaveStatus] = useState<Record<number, SaveStatus>>({});

    const handleSaveComp = async (index: number, placements: UnitPlacementDTO[]) =>
    {
        if (!token) return;

        setSaveStatus(prev => ({...prev, [index]: 'saving'}));
        try {
            await compsService.saveComp(token, placements.map(placement => ({
                unitApiName: placement.unit.apiName,
                row: placement.row,
                col: placement.col
            })));
            setSaveStatus(prev => ({...prev, [index]: 'saved'}));
        } catch (err) {
            console.error('Error saving comp:', err);
            setSaveStatus(prev => ({...prev, [index]: 'error'}));
        }
    };

    const [basicInputs, setBasicInputs] = useState<BasicInputs>({
        tacticianLevel: 1,
        requiredUnits: [],
        requiredTraits: []
    });

    const [advancedInputs, setAdvancedInputs] = useState<AdvancedInputs>({
        crownsPans: 0,
        luck: 0.5,
        targetGold: 50,
        emblems: []
    });




    const [showAdvanced, setShowAdvanced] = useState(false);
    const [isCalculating, setIsCalculating] = useState(false);
    const [isResetting, setIsResetting] = useState(false);
    const [result, setResult] = useState<TFTCompositionResult | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [resetKey, setResetKey] = useState(0);




    // Search states for emblems
    const emblemSearchRef = useRef<EmblemSearchBoxHandle>(null);

    // Search states for traits
    const traitSearchRef = useRef<TraitSearchBoxHandle>(null);

    // Search states for units
    const unitSearchRef = useRef<UnitSearchBoxHandle>(null);




    const handleCalculate = async () =>
    {
        setIsCalculating(true);
        setResult(null);
        setError(null);

        try {
            // Collect data from refs
            const selectedUnits = unitSearchRef.current?.getSelectedUnits() || [];
            const selectedTraits = traitSearchRef.current?.getSelectedTraits() || [];
            const selectedEmblems = emblemSearchRef.current?.getSelectedEmblems() || [];

            // Map frontend data to backend HorizontalDTO format
            const horizontalData: HorizontalDTO = {
                compSize: 8, // Default composition size, could be made configurable
                requiredTraits: selectedTraits.reduce((acc, trait) => {
                    acc[trait.apiName] = trait.count;
                    return acc;
                }, {} as Record<string, number>),
                requiredUnits: selectedUnits.map(unit => ({
                    apiName: unit.apiName
                })),
                excludedTraits: [], // Not implemented in UI yet
                excludedUnits: [], // Not implemented in UI yet
                costOfBoard: advancedInputs.targetGold,
                tacticianLevel: basicInputs.tacticianLevel,
                crowns: advancedInputs.crownsPans,
                emblems: selectedEmblems.map(emblem => ({
                    apiName: emblem.apiName
                })),
                luck: advancedInputs.luck
            };

            // Make API call and render exactly what the backend returned
            const compositions = await generateHorizontalComposition(horizontalData);

            setResult({
                compositions: compositions ?? []
            });
        } catch (err) {
            console.error('Error generating composition:', err);
            setError('Error generating composition. Please try again.');
        } finally {
            setIsCalculating(false);
        }
    };

    const resetForm = async () =>
    {
        setIsResetting(true);

        setResult(null);
        setError(null);

        setTimeout(() =>
        {
            setBasicInputs({
                tacticianLevel: 1,
                requiredUnits: [],
                requiredTraits: []
            });
            setAdvancedInputs({
                crownsPans: 0,
                luck: 0.5,
                targetGold: 50,
                emblems: []
            });

            emblemSearchRef.current?.clearSelection();

            setShowAdvanced(false);
            setResetKey(prev => prev + 1);

            setTimeout(() =>
            {
                setIsResetting(false);
            }, 600);
        }, 200);
    };

    return (
        <div className="flex-1 p-6 bg-background">
            <div className="max-w-4xl mx-auto">
                <motion.div
                    className="text-center mb-8"
                    initial={{opacity: 0, y: -20}}
                    animate={{opacity: 1, y: 0}}
                    transition={{duration: 0.5}}
                >
                    <h1 className="text-4xl font-bold text-primary mb-2">
                        TFT Composition Generator
                    </h1>
                    <p className="text-lg text-secondary">
                        Build optimal Teamfight Tactics compositions with strategic precision
                    </p>
                </motion.div>

                {/* Basic Inputs Card */}
                <motion.div
                    key={`basic-${resetKey}`}
                    className="bg-card border border-border rounded-lg p-6 shadow-sm mb-6"
                    initial={{opacity: 0, y: 20, scale: 0.95}}
                    animate={{
                        opacity: 1,
                        y: 0,
                        scale: 1,
                        boxShadow: isResetting ? "0 0 20px rgba(59, 130, 246, 0.3)" : "0 1px 3px 0 rgb(0 0 0 / 0.1)"
                    }}
                    transition={{
                        duration: 0.5,
                        delay: 0.1,
                        boxShadow: {duration: 0.3}
                    }}
                >
                    <motion.div
                        animate={isResetting ? {
                            scale: [1, 0.98, 1],
                            opacity: [1, 0.7, 1]
                        } : {}}
                        transition={{duration: 0.4}}
                    >
                        <h2 className="text-lg font-semibold text-primary mb-4">Basic Parameters</h2>

                        {/* Tactician Level */}
                        <div className="mb-6">
                            <label className="block text-sm font-medium text-primary mb-2">
                                Tactician Level (1-10)
                            </label>
                            <div className="flex items-center gap-4">
                                <input
                                    type="range"
                                    min="1"
                                    max="10"
                                    value={basicInputs.tacticianLevel}
                                    onChange={(e) => setBasicInputs(prev => ({
                                        ...prev,
                                        tacticianLevel: parseInt(e.target.value)
                                    }))}
                                    className="flex-1"
                                />
                                <span className="text-2xl font-bold text-primary w-12 text-center">
                                    {basicInputs.tacticianLevel}
                                </span>
                            </div>
                        </div>

                        {/* Unit Search Box */}
                        <UnitSearchBox ref={unitSearchRef}/>

                        {/* Emblem Search Box*/}
                        <TraitSearchBox ref={traitSearchRef}/>

                    </motion.div>
                </motion.div>

                {/* Advanced Inputs Card */}
                <motion.div
                    className="bg-card border border-border rounded-lg shadow-sm mb-6 overflow-hidden"
                    initial={{opacity: 0, y: 20, scale: 0.95}}
                    animate={{opacity: 1, y: 0, scale: 1}}
                    transition={{duration: 0.5, delay: 0.15}}
                >
                    {/* Advanced Toggle Header */}
                    <motion.button
                        onClick={() => setShowAdvanced(!showAdvanced)}
                        className="flex items-center justify-between w-full px-6 py-4 bg-accent hover:bg-accent/80 transition-colors duration-200 border-b border-border"
                        whileHover={{scale: 1.005}}
                        whileTap={{scale: 0.995}}
                        animate={isResetting ? {
                            scale: [1, 0.95, 1],
                            backgroundColor: ["var(--accent)", "rgba(59, 130, 246, 0.1)", "var(--accent)"]
                        } : {}}
                        transition={{duration: 0.4, delay: 0.3}}
                    >
                        <span className="text-lg font-medium text-primary">Advanced Parameters</span>
                        <motion.span
                            animate={{rotate: showAdvanced ? 180 : 0}}
                            transition={{duration: 0.3, ease: "easeInOut"}}
                            className="text-xl"
                        >
                            ↓
                        </motion.span>
                    </motion.button>

                    {/* Advanced Content */}
                    <AnimatePresence>
                        {showAdvanced && (
                            <motion.div
                                key={`advanced-content-${resetKey}`}
                                initial={{opacity: 0, height: 0}}
                                animate={{opacity: 1, height: "auto"}}
                                exit={{opacity: 0, height: 0}}
                                transition={{
                                    duration: 0.4,
                                    ease: "easeInOut"
                                }}
                                className="overflow-hidden"
                            >
                                <motion.div
                                    initial={{y: -20, opacity: 0}}
                                    animate={{y: 0, opacity: 1}}
                                    exit={{y: -20, opacity: 0}}
                                    transition={{duration: 0.3, delay: 0.1}}
                                    className="p-6"
                                >
                                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                                        <div>
                                            <label className="block text-sm font-medium text-primary mb-2">
                                                Tactician Crowns/Pans
                                            </label>
                                            <input
                                                type="number"
                                                min="0"
                                                max="3"
                                                value={advancedInputs.crownsPans}
                                                onChange={(e) => setAdvancedInputs(prev => ({
                                                    ...prev,
                                                    crownsPans: parseInt(e.target.value)
                                                }))}
                                                className="w-full px-3 py-2 border border-border rounded-md bg-primary text-primary focus:outline-none focus:ring-1 focus:ring-border "
                                            />
                                        </div>

                                        <div>
                                            <label className="block text-sm font-medium text-primary mb-2">
                                                Luck Factor (0.0 - 1.0)
                                            </label>
                                            <input
                                                type="number"
                                                min="0"
                                                max="1"
                                                step="0.1"
                                                value={advancedInputs.luck}
                                                onChange={(e) => setAdvancedInputs(prev => ({
                                                    ...prev,
                                                    luck: parseFloat(e.target.value)
                                                }))}
                                                className="w-full px-3 py-2 border border-border rounded-md bg-primary text-primary focus:outline-none focus:ring-1 focus:ring-border "
                                            />
                                        </div>

                                        <div>
                                            <label className="block text-sm font-medium text-primary mb-2">
                                                Target Gold Threshold
                                            </label>
                                            <input
                                                type="number"
                                                min="0"
                                                max="100"
                                                value={advancedInputs.targetGold}
                                                onChange={(e) => setAdvancedInputs(prev => ({
                                                    ...prev,
                                                    targetGold: parseInt(e.target.value)
                                                }))}
                                                className="w-full px-3 py-2 border border-border rounded-md bg-primary text-primary focus:outline-none focus:ring-1 focus:ring-border "
                                            />
                                        </div>
                                    </div>

                                    {/* Emblems */}
                                    <EmblemSearchBox ref={emblemSearchRef}/>
                                </motion.div>
                            </motion.div>
                        )}
                    </AnimatePresence>
                </motion.div>

                {/* Action Buttons Card */}
                <motion.div
                    className="bg-card border border-border rounded-lg p-6 shadow-sm mb-6"
                    initial={{opacity: 0, y: 20, scale: 0.95}}
                    animate={{opacity: 1, y: 0, scale: 1}}
                    transition={{duration: 0.5, delay: 0.2}}
                >
                    <div className="flex flex-col sm:flex-row gap-4">
                        <motion.button
                            onClick={handleCalculate}
                            disabled={isCalculating || basicInputs.tacticianLevel < 1}
                            className="flex-1 px-6 py-3 bg-secondary text-primary rounded-lg font-medium hover:bg-accent/90 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200"
                            whileHover={{scale: 1.02}}
                            whileTap={{scale: 0.98}}
                        >
                            {isCalculating ? (
                                <span className="flex items-center justify-center">
                                    <motion.span
                                        className="mr-2"
                                        animate={{rotate: 360}}
                                        transition={{duration: 1, repeat: Infinity, ease: "linear"}}
                                    >
                                        ⟳
                                    </motion.span>
                                    Generating Composition...
                                </span>
                            ) : (
                                'Generate TFT Composition'
                            )}
                        </motion.button>

                        <motion.button
                            onClick={resetForm}
                            disabled={isResetting}
                            className="px-6 py-3 bg-secondary text-primary rounded-lg font-medium hover:bg-secondary/90 disabled:opacity-70 transition-colors duration-200"
                            whileHover={{scale: isResetting ? 1 : 1.02}}
                            whileTap={{scale: isResetting ? 1 : 0.98}}
                            animate={isResetting ? {
                                scale: [1, 1.05, 1],
                                backgroundColor: ["var(--secondary)", "#ef4444", "var(--secondary)"]
                            } : {}}
                            transition={{duration: 0.4}}
                        >
                            {isResetting ? (
                                <span className="flex items-center justify-center">
                                    <motion.span
                                        className="mr-2"
                                        animate={{rotate: [0, -90, -180, -270, -360]}}
                                        transition={{duration: 0.6, ease: "easeInOut"}}
                                    >
                                        🔄
                                    </motion.span>
                                    Resetting...
                                </span>
                            ) : (
                                'Reset Form'
                            )}
                        </motion.button>
                    </div>
                </motion.div>

                {/* Error Card */}
                <AnimatePresence>
                    {error && (
                        <motion.div
                            initial={{opacity: 0, y: 30, scale: 0.95}}
                            animate={{opacity: 1, y: 0, scale: 1}}
                            exit={{opacity: 0, y: -30, scale: 0.95}}
                            transition={{duration: 0.5, ease: "easeOut"}}
                            className="bg-red-50 border border-red-200 rounded-lg p-6 shadow-sm mb-6"
                        >
                            <div className="text-sm font-medium text-red-800 mb-1">Error</div>
                            <div className="text-red-700">{error}</div>
                        </motion.div>
                    )}
                </AnimatePresence>

                {/* Results Card */}
                <AnimatePresence>
                    {result && (
                        <motion.div
                            initial={{opacity: 0, y: 30, scale: 0.95}}
                            animate={{opacity: 1, y: 0, scale: 1}}
                            exit={{opacity: 0, y: -30, scale: 0.95}}
                            transition={{duration: 0.5, ease: "easeOut"}}
                            className="bg-card border border-border rounded-lg p-6 shadow-sm"
                        >
                            <div className="flex items-center justify-center mb-4">
                                <h3 className="text-2xl font-semibold text-primary flex items-center">
                                    <span className="text-green-600 mr-2">⚔️</span>
                                    TFT Composition Results
                                </h3>
                            </div>

                            {result.compositions.length === 0 ? (
                                <div className="p-4 bg-accent/50 rounded-lg text-primary">
                                    No valid compositions found with the given parameters.
                                </div>
                            ) : (
                                <div className="space-y-6">
                                    {result.compositions.map((composition, index) => {
                                        const board = placementsToBoard(composition.placements);
                                        const traitBreakdown = computeTraitSummary(Object.values(board));
                                        const status = saveStatus[index] ?? 'idle';
                                        return (
                                            <motion.div
                                                key={composition.teamCode || index}
                                                className="border border-border rounded-lg p-4"
                                                initial={{opacity: 0, y: 20}}
                                                animate={{opacity: 1, y: 0}}
                                                transition={{delay: 0.1 * index}}
                                            >
                                                <h4 className="text-lg font-semibold text-primary text-center mb-3">
                                                    Composition {index + 1}
                                                </h4>

                                                {/* Hex Board + Active Traits sidebar */}
                                                <div className="flex justify-center gap-4 mb-4">
                                                    <HexBoard board={board} readOnly />

                                                    {traitBreakdown.length > 0 && (
                                                        <div className="w-56 shrink-0 border-l border-border bg-primary p-3 rounded-r-lg">
                                                            <h5 className="text-sm font-bold text-primary mb-3">Active Traits</h5>
                                                            <div className="flex flex-col gap-2">
                                                                {traitBreakdown.map((trait) => (
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

                                                {/* Team Code + Comp Builder handoff + Save */}
                                                <div className="flex items-center justify-center gap-2">
                                                    {composition.teamCode && (
                                                        <button
                                                            onClick={() => navigator.clipboard.writeText(composition.teamCode)}
                                                            className="px-3 py-2 bg-secondary text-primary rounded-md text-sm font-medium hover:bg-secondary/90 transition-colors duration-200"
                                                        >
                                                            Copy Team Code
                                                        </button>
                                                    )}
                                                    <button
                                                        onClick={() => navigate('/tools/comp-builder', {
                                                            state: {seedBoard: board}
                                                        })}
                                                        className="px-3 py-2 bg-accent text-primary rounded-md text-sm font-medium hover:bg-accent/80 transition-colors duration-200"
                                                    >
                                                        Edit in Comp Builder
                                                    </button>
                                                    <button
                                                        onClick={() => handleSaveComp(index, composition.placements)}
                                                        disabled={!user || status === 'saving' || status === 'saved'}
                                                        title={!user ? 'Log in to save comps' : undefined}
                                                        className="px-3 py-2 bg-secondary text-primary rounded-md text-sm font-medium hover:bg-secondary/90 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200"
                                                    >
                                                        {status === 'saving' ? 'Saving...' : status === 'saved' ? 'Saved' : status === 'error' ? 'Retry Save' : !user ? 'Log in to Save' : 'Save Comp'}
                                                    </button>
                                                </div>
                                                {status === 'error' && (
                                                    <div className="text-center text-xs text-red-600 mt-2">
                                                        Failed to save comp. Please try again.
                                                    </div>
                                                )}
                                            </motion.div>
                                        );
                                    })}
                                </div>
                            )}
                        </motion.div>
                    )}
                </AnimatePresence>
            </div>
        </div>
    );
};

export default HorizontalCompositionGenerator;