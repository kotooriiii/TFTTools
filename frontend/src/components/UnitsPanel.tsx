import React, {useState} from 'react';
import {Unit} from '../types/unitTypes';
import {SelectedItem} from '../types/searchTypes';
import {UnitGridView} from './UnitGridView';
import {UnitDetailedView} from './UnitDetailedView';
import {JumpingDots} from "./JumpingDots.tsx";

interface UnitsPanelProps
{
    filteredUnits: Unit[];
    onUnitDragStart: (e: React.DragEvent, unit: Unit) => void;
    selectedItems: SelectedItem[];
    isLoading: boolean;
}

export const UnitsPanel: React.FC<UnitsPanelProps> = ({
                                                          filteredUnits,
                                                          onUnitDragStart,
                                                          selectedItems,
                                                          isLoading
                                                      }) =>
{
    const [unitsViewMode, setUnitsViewMode] = useState<'grid' | 'detailed'>('grid');

    return (
        <div style={{
            position: 'absolute',
            top: '20px',
            left: '20px',
            width: '320px',
            maxHeight: '400px',
            backgroundColor: 'var(--color-bg-primary)',
            border: '2px solid var(--color-border)',
            borderRadius: '12px',
            padding: '16px',
            boxShadow: '0 4px 20px rgba(0, 0, 0, 0.15)',
            backdropFilter: 'blur(10px)',
            zIndex: 1000,
            overflowY: 'auto'
        }}>
            <h3 style={{
                margin: '0 0 8px 0',
                fontSize: '16px',
                fontWeight: 'bold',
                color: 'var(--color-text-primary)'
            }}>
                Filtered Units ({filteredUnits.length})
            </h3>

            {/* View Toggle Buttons */}
            <div style={{
                display: 'flex',
                gap: '4px',
                backgroundColor: 'var(--color-bg-secondary)',
                borderRadius: '6px',
                padding: '2px',
                marginBottom: '16px'
            }}>
                <button
                    onClick={() => setUnitsViewMode('grid')}
                    style={{
                        fontSize: '10px',
                        padding: '4px 8px',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        backgroundColor: unitsViewMode === 'grid' ? 'var(--color-bg-accent)' : 'transparent',
                        color: unitsViewMode === 'grid' ? 'var(--color-text-accent)' : 'var(--color-text-secondary)',
                        transition: 'all 0.2s ease'
                    }}
                    title="Grid View"
                >
                    ⊞
                </button>
                <button
                    onClick={() => setUnitsViewMode('detailed')}
                    style={{
                        fontSize: '10px',
                        padding: '4px 8px',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        backgroundColor: unitsViewMode === 'detailed' ? 'var(--color-bg-accent)' : 'transparent',
                        color: unitsViewMode === 'detailed' ? 'var(--color-text-accent)' : 'var(--color-text-secondary)',
                        transition: 'all 0.2s ease'
                    }}
                    title="Detailed View"
                >
                    ☰
                </button>
            </div>

            <p style={{
                margin: '0 0 16px 0',
                fontSize: '11px',
                color: 'var(--color-text-secondary)',
                fontStyle: 'italic'
            }}>
                Drag units to the graph canvas
            </p>

            {/* Loading State */}
            {isLoading &&
                <div
                    style={{
                        minHeight: '75px',
                        position: 'relative',
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        justifyContent: 'center'
                    }}
                >
                    <JumpingDots/>
                </div>
            }

            {/* Content Views - only show when not loading */}
            {!isLoading && (
                unitsViewMode === 'grid' ? (
                    <UnitGridView
                        units={filteredUnits}
                        onUnitDragStart={onUnitDragStart}
                        selectedItems={selectedItems}
                    />
                ) : (
                    <UnitDetailedView
                        units={filteredUnits}
                        onUnitDragStart={onUnitDragStart}
                        selectedItems={selectedItems}
                    />
                )
            )}

        </div>
    );
};