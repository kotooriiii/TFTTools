import React from 'react';
import { motion } from 'framer-motion';
import { Unit } from '../types/unitTypes';
import { SelectedItem } from '../types/searchTypes';

interface UnitDetailedViewProps {
    units: Unit[];
    onUnitDragStart: (e: React.DragEvent, unit: Unit) => void;
    selectedItems: SelectedItem[];
}

export const UnitDetailedView: React.FC<UnitDetailedViewProps> = ({
                                                                      units,
                                                                      onUnitDragStart,
                                                                      selectedItems
                                                                  }) => {
    return (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            {units.map((unit, index) => (
                <motion.div
                    key={`${unit.champion}-${index}-detailed`}
                    initial={{ opacity: 0, x: -20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: index * 0.03 }}
                    draggable
                    onDragStart={(e) => onUnitDragStart(e, unit)}
                    style={{
                        padding: '12px',
                        backgroundColor: 'white',
                        border: '2px solid #C3A995',
                        borderRadius: '8px',
                        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
                        cursor: 'grab',
                        display: 'flex',
                        alignItems: 'center',
                        gap: '12px',
                        transition: 'all 0.2s ease'
                    }}
                    whileHover={{
                        x: 4,
                        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.15)',
                        borderColor: '#8B7355'
                    }}
                    whileTap={{ scale: 0.98 }}
                >
                    {/* Champion Icon */}
                    <div style={{
                        width: '48px',
                        height: '48px',
                        backgroundColor: '#6F5E53',
                        borderRadius: '8px',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        color: 'white',
                        fontSize: '20px',
                        fontWeight: 'bold',
                        flexShrink: 0,
                        position: 'relative'
                    }}>
                        ⚔️
                        <div style={{
                            position: 'absolute',
                            top: '2px',
                            right: '2px',
                            fontSize: '8px',
                            opacity: 0.7
                        }}>
                            ⋮⋮
                        </div>
                    </div>

                    {/* Unit Info */}
                    <div style={{ flex: 1, minWidth: 0 }}>
                        <div style={{
                            fontSize: '14px',
                            fontWeight: 'bold',
                            color: '#6F5E53',
                            marginBottom: '6px'
                        }}>
                            {unit.champion.toLowerCase().replace('_', ' ')}
                        </div>

                        <div style={{
                            display: 'flex',
                            flexWrap: 'wrap',
                            gap: '4px',
                            marginBottom: '4px'
                        }}>
                            {unit.traits.map(trait => (
                                <span
                                    key={trait}
                                    style={{
                                        fontSize: '10px',
                                        backgroundColor: selectedItems.some(item =>
                                            item.type === 'trait' &&
                                            item.name.toUpperCase() === trait
                                        ) ? '#8B7355' : '#E5E5E5',
                                        color: selectedItems.some(item =>
                                            item.type === 'trait' &&
                                            item.name.toUpperCase() === trait
                                        ) ? 'white' : '#666',
                                        padding: '2px 6px',
                                        borderRadius: '8px',
                                        fontWeight: selectedItems.some(item =>
                                            item.type === 'trait' &&
                                            item.name.toUpperCase() === trait
                                        ) ? 'bold' : 'normal'
                                    }}
                                >
                                    {trait.toLowerCase().replace('_', ' ')}
                                </span>
                            ))}
                        </div>

                        <div style={{
                            fontSize: '9px',
                            color: '#888',
                            fontStyle: 'italic'
                        }}>
                            {unit.traits.length} trait{unit.traits.length !== 1 ? 's' : ''}
                        </div>
                    </div>

                    <div style={{
                        fontSize: '12px',
                        color: '#999',
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        gap: '2px'
                    }}>
                        <div>→</div>
                        <div style={{ fontSize: '8px' }}>drag</div>
                    </div>
                </motion.div>
            ))}
        </div>
    );
};