import React from 'react';
import { motion } from 'framer-motion';
import { Edge, Vertex } from '../types/graphTypes';
import { CIRCLE_RADIUS } from '../types/graphTypes';

interface GraphEdgesProps {
    edges: Edge[];
    vertices: Vertex[];
    draggingId: number | null;
    hoveringId: number | null;
}

export const GraphEdges: React.FC<GraphEdgesProps> = ({
                                                          edges,
                                                          vertices,
                                                          draggingId,
                                                          hoveringId
                                                      }) => {
    const getVertexRadius = (vertexId: number) => {
        const baseRadius = CIRCLE_RADIUS;
        if (draggingId === vertexId) return baseRadius * 0.9;
        if (hoveringId === vertexId) return baseRadius * 1.1;
        return baseRadius;
    };


    const getTransition = () =>
    {
        return {
            type: "spring",
            stiffness: 40000,
            damping: 200,
            mass: 0.1
        };
    };


        return (
        <>
            {edges.map((edge) => {
                const sourceVertex = vertices.find(v => v.id === edge.sourceId);
                const targetVertex = vertices.find(v => v.id === edge.targetId);

                if (!sourceVertex || !targetVertex) return null;

                const sourceRadius = getVertexRadius(sourceVertex.id);
                const targetRadius = getVertexRadius(targetVertex.id);

                // Calculate edge endpoints considering vertex radius
                const dx = targetVertex.x - sourceVertex.x;
                const dy = targetVertex.y - sourceVertex.y;
                const distance = Math.sqrt(dx * dx + dy * dy);

                // Avoid division by zero
                if (distance === 0) return null;

                const sourceOffsetX = (dx / distance) * sourceRadius;
                const sourceOffsetY = (dy / distance) * sourceRadius;
                const targetOffsetX = (dx / distance) * targetRadius;
                const targetOffsetY = (dy / distance) * targetRadius;

                const startX = sourceVertex.x + sourceOffsetX;
                const startY = sourceVertex.y + sourceOffsetY;
                const endX = targetVertex.x - targetOffsetX;
                const endY = targetVertex.y - targetOffsetY;

                const midX = (startX + endX) / 2;
                const midY = (startY + endY) / 2;

                const transition = getTransition();

                return (
                    <g key={edge.id}>
                        <motion.line
                            x1={startX}
                            y1={startY}
                            x2={endX}
                            y2={endY}
                            stroke="#594b42"
                            strokeWidth={2}
                            initial={false}
                            animate={{
                                x1: startX,
                                y1: startY,
                                x2: endX,
                                y2: endY
                            }}
                            transition={transition}
                        />
                        {edge.label && (
                            <g>
                                <motion.rect
                                    width={edge.label.length * 7}
                                    height={16}
                                    fill="white"
                                    stroke="#594b42"
                                    strokeWidth={1}
                                    rx={2}
                                    initial={false}
                                    animate={{
                                        x: midX - (edge.label.length * 3.5),
                                        y: midY - 8
                                    }}
                                    transition={transition}
                                />
                                <motion.text
                                    textAnchor="middle"
                                    dominantBaseline="middle"
                                    fill="#594b42"
                                    fontSize="12"
                                    fontWeight="bold"
                                    style={{ userSelect: 'none' }}
                                    initial={false}
                                    animate={{
                                        x: midX,
                                        y: midY
                                    }}
                                    transition={transition}
                                >
                                    {edge.label}
                                </motion.text>
                            </g>
                        )}
                    </g>
                );
            })}
        </>
    );
};