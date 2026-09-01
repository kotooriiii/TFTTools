import React, {useState} from 'react';
import {Unit} from '../types/unitTypes';
import {SelectedItem} from '../types/searchTypes';
import {UnitGridView} from './UnitGridView';
import {UnitDetailedView} from './UnitDetailedView';
import {JumpingDots} from "./JumpingDots.tsx";
import {Button} from "./Button";

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
                <Button
                    variant="ghost"
                    tone="accent"
                    selected={unitsViewMode === 'grid'}
                    onClick={() => setUnitsViewMode('grid')}
                    className={`text-[10px] px-2 py-1 rounded ${unitsViewMode === 'grid' ? '' : 'text-secondary'}`}
                    title="Grid View"
                >
                    ⊞
                </Button>
                <Button
                    variant="ghost"
                    tone="accent"
                    selected={unitsViewMode === 'detailed'}
                    onClick={() => setUnitsViewMode('detailed')}
                    className={`text-[10px] px-2 py-1 rounded ${unitsViewMode === 'detailed' ? '' : 'text-secondary'}`}
                    title="Detailed View"
                >
                    ☰
                </Button>
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