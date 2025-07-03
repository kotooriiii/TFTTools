import React from 'react';
import { motion } from 'framer-motion';
import { Vertex } from '../types/graphTypes';
import { CIRCLE_RADIUS } from '../types/graphTypes';

interface GraphVertexProps {
    vertex: Vertex;
    isDragging: boolean;
    isHovering: boolean;
    onPointerEnter: () => void;
    onPointerLeave: () => void;
    onPointerDown: (e: React.PointerEvent) => void;
    onPointerMove: (e: React.PointerEvent) => void;
    onPointerUp: (e: React.PointerEvent) => void;
    mousePosition: { x: number; y: number } | null;
    highlightedUnitId: number | null;
}

export const GraphVertex: React.FC<GraphVertexProps> = ({
                                                            vertex,
                                                            isDragging,
                                                            isHovering,
                                                            onPointerEnter,
                                                            onPointerLeave,
                                                            onPointerDown,
                                                            onPointerMove,
                                                            onPointerUp,
                                                            mousePosition,
                                                            highlightedUnitId,
                                                        }) => {
    const getRadius = () => {
        if (isDragging) return CIRCLE_RADIUS * 0.9;
        if (isHovering) return CIRCLE_RADIUS * 1.1;
        return CIRCLE_RADIUS;
    };

    const getColor = () => {
        return '#8B7355';
    };
    const getTooltipPosition = () => {
        if (!mousePosition) return { x: vertex.x, y: vertex.y - 45 };

        const baseOffsetX = 0;
        const baseOffsetY = 60;

        return {
            x: mousePosition.x + (baseOffsetX ),
            y: mousePosition.y - (baseOffsetY )
        };
    };

    const tooltipPos = getTooltipPosition();

    return (
        <g>
            <motion.circle
                r={getRadius()}
                fill={getColor()}
                stroke={vertex.id === highlightedUnitId ? 'yellow' : '#594b42'}
                strokeWidth={vertex.id === highlightedUnitId ? 2 : 1.5}
                style={{ cursor: isDragging ? 'grabbing' : 'grab' }}
                onPointerEnter={onPointerEnter}
                onPointerLeave={onPointerLeave}
                onPointerDown={onPointerDown}
                onPointerMove={onPointerMove}
                onPointerUp={onPointerUp}
                initial={false}
                animate={{
                    cx: vertex.x,
                    cy: vertex.y,
                    r: getRadius(),
                    fill: getColor(),
                    scale: vertex.id === highlightedUnitId
                        ? [1, 1.1, 1] // pulse animation
                        : 1
                }}
                transition={{
                    scale:
                        {
                            duration: 0.5,
                            repeat: vertex.id === highlightedUnitId ? 2 : 0
                        },

                    type: "spring",
                    stiffness: 30000,
                    damping: 200,
                    bounce: 2,
                    velocity: 500,
                    restDelta: 0.001,
                    mass: 0.1
                }}

            />


            {/* Unit tooltip on hover */}
            {vertex.unitData && isHovering && mousePosition && (
                <motion.g
                    initial={{ opacity: 0, scale: 0.8 }}
                    animate={{ opacity: 1, scale: 1 }}
                    style={{ pointerEvents: 'none' }}
                >
                    <motion.rect
                        width={120}
                        height={30}
                        fill="rgba(255, 255, 255, 0.75)"
                        stroke="rgba(89, 75, 66, 0.2)"
                        strokeWidth={1}
                        rx={12}

                        initial={false}
                        animate={{
                            x: tooltipPos.x - 60,
                            y: tooltipPos.y
                        }}
                        transition={{
                            type: "spring",
                            stiffness: 300,
                            damping: 15,
                            mass: 0.8,
                            bounce: 0.25
                        }}



                    />
                    <motion.text
                        textAnchor="middle"
                        dominantBaseline="middle"
                        fill="#6F5E53"
                        fontSize="12"
                        fontWeight="500"
                        initial={false}
                        animate={{
                            x: tooltipPos.x,
                            y: tooltipPos.y + 12.5
                        }}
                        transition={{
                            type: "spring",
                            stiffness: 300,
                            damping: 15,
                            mass: 0.8,
                            bounce: 0.25
                        }}

                        style={{
                            letterSpacing: '0.5px',
                            userSelect: 'none'
                        }}

                    >
                        {vertex.unitData.champion.toLowerCase().replace('_', ' ')}
                    </motion.text>
                </motion.g>
            )}

        </g>
    );
};