import React from 'react';
import {Unit} from '../types/unitTypes';
import {SelectedItem} from '../types/searchTypes';
import {useUnitFiltering} from '../hooks/useUnitFiltering';
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
    const {unitsViewMode, setUnitsViewMode} = useUnitFiltering(selectedItems);

    return (
        <div style={{
            position: 'absolute',
            top: '20px',
            left: '20px',
            width: '320px',
            maxHeight: '400px',
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
            border: '2px solid #594b42',
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
                color: '#594b42'
            }}>
                Filtered Units ({filteredUnits.length})
            </h3>

            {/* View Toggle Buttons */}
            <div style={{
                display: 'flex',
                gap: '4px',
                backgroundColor: '#F5F5F5',
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
                        backgroundColor: unitsViewMode === 'grid' ? '#6F5E53' : 'transparent',
                        color: unitsViewMode === 'grid' ? 'white' : '#666',
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
                        backgroundColor: unitsViewMode === 'detailed' ? '#6F5E53' : 'transparent',
                        color: unitsViewMode === 'detailed' ? 'white' : '#666',
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
                color: '#666',
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

            {unitsViewMode === 'grid' ? (
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
            )}
        </div>
    );
};