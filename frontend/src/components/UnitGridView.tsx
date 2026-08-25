import React from 'react';
import { motion } from 'framer-motion';
import { Unit } from '../types/unitTypes';
import { SelectedItem } from '../types/searchTypes';
import { UnitPortrait } from './UnitPortrait.tsx';

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
                    key={`${unit.displayName}-${index}`}
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
                    {/* Unit Icon */}
                    <div style={{ marginBottom: '4px', flexShrink: 0 }}>
                        <UnitPortrait
                            displayName={unit.displayName}
                            iconUrl={unit.iconUrl}
                            size={36}
                        />
                    </div>

                    {/* Unit Name */}
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
                        title={unit.displayName}
                    >
                        {unit.displayName}
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
                                key={trait.displayName}
                                title={trait.displayName}
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
                                        item.displayName === trait.displayName
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