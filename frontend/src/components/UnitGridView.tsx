import React from 'react';
import { motion } from 'framer-motion';
import { Unit } from '../types/unitTypes';
import { SelectedItem } from '../types/searchTypes';

interface UnitGridViewProps {
    units: Unit[];
    onUnitDragStart: (e: React.DragEvent, unit: Unit) => void;
    selectedItems: SelectedItem[];
}

export const UnitGridView: React.FC<UnitGridViewProps> = ({
                                                              units,
                                                              onUnitDragStart,
                                                              selectedItems
                                                          }) => {
    return (
        <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fill, minmax(90px, 1fr))',
            gap: '12px',
            justifyItems: 'center'
        }}>
            {units.map((unit, index) => (
                <motion.div
                    key={`${unit.champion}-${index}`}
                    initial={{ opacity: 0, scale: 0.8 }}
                    animate={{ opacity: 1, scale: 1 }}
                    transition={{ delay: index * 0.02 }}
                    draggable
                    onDragStart={(e) => onUnitDragStart(e, unit)}
                    style={{
                        width: '80px',
                        height: '90px',
                        backgroundColor: 'white',
                        border: '2px solid #C3A995',
                        borderRadius: '8px',
                        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
                        cursor: 'grab',
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        justifyContent: 'space-between',
                        padding: '8px 4px',
                        transition: 'all 0.2s ease',
                        position: 'relative',
                        overflow: 'visible',
                    }}
                    whileHover={{
                        scale: 1.05,
                        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.15)',
                        borderColor: '#8B7355'
                    }}
                    whileTap={{ scale: 0.95 }}
                >
                    {/* Champion Icon */}
                    <div style={{
                        width: '36px',
                        height: '36px',
                        backgroundColor: '#6F5E53',
                        borderRadius: '6px',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        color: 'white',
                        fontSize: '18px',
                        fontWeight: 'bold',
                        marginBottom: '4px',
                        flexShrink: 0
                    }}>
                        ⚔️
                    </div>

                    {/* Champion Name */}
                    <div
                        style={{
                            fontSize: '10px',
                            fontWeight: 'bold',
                            paddingBottom: '4px',
                            color: '#6F5E53',
                            textAlign: 'center',
                            lineHeight: '1.3',
                            overflow: 'hidden',
                            textOverflow: 'ellipsis',
                            whiteSpace: 'nowrap',
                            width: '100%',
                        }}
                        title={unit.champion}
                    >
                        {unit.champion}
                    </div>

                    {/* Trait indicators */}
                    <div style={{
                        display: 'flex',
                        flexWrap: 'wrap',
                        gap: '2px',
                        justifyContent: 'center',
                        alignItems: 'center',
                        minHeight: '16px',
                        position: 'relative'
                    }}>
                        {unit.traits.slice(0, 4).map(trait => (
                            <div
                                key={trait}
                                title={trait}
                                style={{
                                    width: '12px',
                                    height: '12px',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    cursor: 'help',
                                    position: 'relative'
                                }}
                            >
                                <div style={{
                                    width: '8px',
                                    height: '8px',
                                    borderRadius: '50%',
                                    backgroundColor: selectedItems.some(item =>
                                        item.type === 'trait' &&
                                        item.name === trait
                                    ) ? '#8B7355' : '#E5E5E5',
                                }} />
                            </div>
                        ))}
                    </div>

                    {/* Drag indicator */}
                    <div style={{
                        position: 'absolute',
                        top: '2px',
                        right: '2px',
                        fontSize: '8px',
                        color: '#999',
                        opacity: 0.7
                    }}>
                        ⋮⋮
                    </div>
                </motion.div>
            ))}
        </div>
    );
};