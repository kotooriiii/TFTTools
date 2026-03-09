import React, {useRef, useState} from 'react';
import {AnimatePresence, motion} from 'framer-motion';
import {EmblemItem} from '../types/searchTypes';

import {EmblemSearchBox, EmblemSearchBoxHandle} from "../components/HorizontalCompositionGenerator/EmblemSearchBox.tsx";
import {TraitSearchBox, TraitSearchBoxHandle} from '../components/HorizontalCompositionGenerator/TraitSearchBox.tsx';
import {
    ChampionSearchBox,
    ChampionSearchBoxHandle
} from "../components/HorizontalCompositionGenerator/ChampionSearchBox.tsx";

interface BasicInputs
{
    tacticianLevel: number;
    requiredChampions: SelectedItem[];
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
    composition: string;
    totalCost: number;
    averageLevel: number;
    traitBreakdowns: { trait: string; count: number; active: boolean }[];
    goldEfficiency: number;
    strengthRating: number;
    timestamp: string;
}

const HorizontalCompositionGenerator: React.FC = () =>
{
    const [basicInputs, setBasicInputs] = useState<BasicInputs>({
        tacticianLevel: 1,
        requiredChampions: [],
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
    const [resetKey, setResetKey] = useState(0);




    // Search states for emblems
    const emblemSearchRef = useRef<EmblemSearchBoxHandle>(null);
    const selectedEmblems = emblemSearchRef.current?.getSelectedEmblems() || [];

    // Search states for traits
    const traitSearchRef = useRef<TraitSearchBoxHandle>(null);
    const selectedTraits = traitSearchRef.current?.getSelectedTraits() || [];

    // Search states for champions
    const championSearchRef = useRef<ChampionSearchBoxHandle>(null);
    const selectedChampions = championSearchRef.current?.getSelectedChampions() || [];




    const handleCalculate = async () =>
    {
        setIsCalculating(true);
        setResult(null);

        // Simulate API call delay
        setTimeout(() =>
        {
            const mockResult: TFTCompositionResult = {
                composition: `Level ${basicInputs.tacticianLevel} composition with ${basicInputs.requiredChampions.length} required champions`,
                totalCost: basicInputs.tacticianLevel * 2 + basicInputs.requiredChampions.length * 3,
                averageLevel: Math.floor(Math.random() * 3) + 1,
                traitBreakdowns: basicInputs.requiredTraits.map(trait => ({
                    trait: trait.trait,
                    count: trait.count,
                    active: trait.count >= 3
                })),
                goldEfficiency: Math.floor(Math.random() * 30 + 70),
                strengthRating: Math.floor(Math.random() * 40 + 60),
                timestamp: new Date().toLocaleString()
            };
            setResult(mockResult);
            setIsCalculating(false);
        }, 1500);
    };

    const resetForm = async () =>
    {
        setIsResetting(true);

        setResult(null);

        setTimeout(() =>
        {
            setBasicInputs({
                tacticianLevel: 1,
                requiredChampions: [],
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
                    <h1 className="text-4xl font-bold text-foreground mb-2">
                        TFT Composition Generator
                    </h1>
                    <p className="text-lg text-muted-foreground">
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
                        <h2 className="text-lg font-semibold text-foreground mb-4">Basic Parameters</h2>

                        {/* Tactician Level */}
                        <div className="mb-6">
                            <label className="block text-sm font-medium text-foreground mb-2">
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

                        {/* Champion Search Box */}
                        <ChampionSearchBox ref={championSearchRef}/>

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
                        <span className="text-lg font-medium text-foreground">Advanced Parameters</span>
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
                                            <label className="block text-sm font-medium text-foreground mb-2">
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
                                                className="w-full px-3 py-2 border border-border rounded-md bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                                            />
                                        </div>

                                        <div>
                                            <label className="block text-sm font-medium text-foreground mb-2">
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
                                                className="w-full px-3 py-2 border border-border rounded-md bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                                            />
                                        </div>

                                        <div>
                                            <label className="block text-sm font-medium text-foreground mb-2">
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
                                                className="w-full px-3 py-2 border border-border rounded-md bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
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
                            className="flex-1 px-6 py-3 bg-secondary text-primary-foreground rounded-lg font-medium hover:bg-accent/90 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200"
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
                            className="px-6 py-3 bg-secondary text-secondary-foreground rounded-lg font-medium hover:bg-secondary/90 disabled:opacity-70 transition-colors duration-200"
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
                            <div className="flex items-center justify-between mb-4">
                                <h3 className="text-2xl font-semibold text-foreground flex items-center">
                                    <span className="text-green-600 mr-2">⚔️</span>
                                    TFT Composition Results
                                </h3>
                                <div className="text-sm text-muted-foreground">
                                    {result.timestamp}
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                                <motion.div
                                    className="bg-accent/50 rounded-lg p-4"
                                    initial={{opacity: 0, x: -20}}
                                    animate={{opacity: 1, x: 0}}
                                    transition={{delay: 0.1}}
                                >
                                    <div className="text-sm font-medium text-muted-foreground mb-1">Composition</div>
                                    <div className="text-lg font-semibold text-foreground">{result.composition}</div>
                                </motion.div>

                                <motion.div
                                    className="bg-blue-50 rounded-lg p-4"
                                    initial={{opacity: 0, x: -20}}
                                    animate={{opacity: 1, x: 0}}
                                    transition={{delay: 0.2}}
                                >
                                    <div className="text-sm font-medium text-muted-foreground mb-1">Total Cost</div>
                                    <div className="text-lg font-semibold text-blue-700">{result.totalCost} Gold</div>
                                </motion.div>

                                <motion.div
                                    className="bg-purple-50 rounded-lg p-4"
                                    initial={{opacity: 0, x: -20}}
                                    animate={{opacity: 1, x: 0}}
                                    transition={{delay: 0.3}}
                                >
                                    <div className="text-sm font-medium text-muted-foreground mb-1">Average Unit Level
                                    </div>
                                    <div className="text-lg font-semibold text-purple-700">⭐ {result.averageLevel}</div>
                                </motion.div>

                                <motion.div
                                    className="bg-orange-50 rounded-lg p-4"
                                    initial={{opacity: 0, x: -20}}
                                    animate={{opacity: 1, x: 0}}
                                    transition={{delay: 0.4}}
                                >
                                    <div className="text-sm font-medium text-muted-foreground mb-1">Gold Efficiency
                                    </div>
                                    <div className="text-lg font-semibold text-orange-700">{result.goldEfficiency}%
                                    </div>
                                </motion.div>

                                <motion.div
                                    className="bg-emerald-50 rounded-lg p-4"
                                    initial={{opacity: 0, x: -20}}
                                    animate={{opacity: 1, x: 0}}
                                    transition={{delay: 0.5}}
                                >
                                    <div className="text-sm font-medium text-muted-foreground mb-1">Strength Rating
                                    </div>
                                    <div
                                        className="text-lg font-semibold text-emerald-700">{result.strengthRating}/100
                                    </div>
                                </motion.div>
                            </div>

                            {/* Trait Breakdowns */}
                            {result.traitBreakdowns.length > 0 && (
                                <motion.div
                                    className="mt-6"
                                    initial={{opacity: 0, y: 20}}
                                    animate={{opacity: 1, y: 0}}
                                    transition={{delay: 0.6}}
                                >
                                    <h4 className="text-lg font-semibold text-foreground mb-3">Trait Synergies</h4>
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                                        {result.traitBreakdowns.map((trait) => (
                                            <div
                                                key={trait.trait}
                                                className={`flex items-center justify-between p-3 rounded-lg ${
                                                    trait.active ? 'bg-green-50 border-green-200' : 'bg-gray-50 border-gray-200'
                                                } border`}
                                            >
                                                <span className="font-medium">{trait.trait}</span>
                                                <div className="flex items-center gap-2">
                                                    <span className="text-sm">{trait.count}</span>
                                                    {trait.active && <span className="text-green-600">✓</span>}
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </motion.div>
                            )}

                            <motion.div
                                className="mt-6 p-4 bg-green-50 border border-green-200 rounded-lg"
                                initial={{opacity: 0, y: 20}}
                                animate={{opacity: 1, y: 0}}
                                transition={{delay: 0.7}}
                            >
                                <div className="text-sm font-medium text-green-800 mb-2">Status</div>
                                <div className="text-green-700">
                                    ✅ TFT composition generated successfully. Ready for ranked play!
                                </div>
                            </motion.div>
                        </motion.div>
                    )}
                </AnimatePresence>
            </div>
        </div>
    );
};

export default HorizontalCompositionGenerator;