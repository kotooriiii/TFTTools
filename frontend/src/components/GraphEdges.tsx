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

    const getTransition = () => {
        return {
            type: "spring",
            stiffness: 40000,
            damping: 200,
            mass: 0.1
        };
    };

    // Function to calculate point on quadratic Bézier curve at t=0.5 (midpoint).
    //- `P(t) = (1-t)²P₀ + 2(1-t)tP₁ + t²P₂`
    const getQuadraticBezierPoint = (start: {x: number, y: number}, control: {x: number, y: number}, end: {x: number, y: number}, t: number = 0.5) => {
        const x = (1 - t) * (1 - t) * start.x + 2 * (1 - t) * t * control.x + t * t * end.x;
        const y = (1 - t) * (1 - t) * start.y + 2 * (1 - t) * t * control.y + t * t * end.y;
        return { x, y };
    };

    return (
        <>
            {edges.map((edge) => {
                const sourceVertex = vertices.find(v => v.id === edge.sourceId);
                const targetVertex = vertices.find(v => v.id === edge.targetId);

                if (!sourceVertex || !targetVertex) return null;

                // Find all edges between the same two vertices
                const parallelEdges = edges.filter(e => 
                    (e.sourceId === edge.sourceId && e.targetId === edge.targetId) ||
                    (e.sourceId === edge.targetId && e.targetId === edge.sourceId)
                );
                
                const edgeIndex = parallelEdges.findIndex(e => e.id === edge.id);
                const totalParallelEdges = parallelEdges.length;
                
                // Calculate curve offset for multiple edges - increased spacing
                const baseOffset = 60; // Increased from 20
                const offsetMultiplier = totalParallelEdges > 1 ? 
                    (edgeIndex - (totalParallelEdges - 1) / 2) * baseOffset : 0;

                const sourceRadius = getVertexRadius(sourceVertex.id);
                const targetRadius = getVertexRadius(targetVertex.id);

                const dx = targetVertex.x - sourceVertex.x;
                const dy = targetVertex.y - sourceVertex.y;
                const distance = Math.sqrt(dx * dx + dy * dy);

                if (distance === 0) return null;

                // Calculate perpendicular offset for curve
                const perpX = -dy / distance * offsetMultiplier;
                const perpY = dx / distance * offsetMultiplier;

                const sourceOffsetX = (dx / distance) * sourceRadius;
                const sourceOffsetY = (dy / distance) * sourceRadius;
                const targetOffsetX = (dx / distance) * targetRadius;
                const targetOffsetY = (dy / distance) * targetRadius;

                const startX = sourceVertex.x + sourceOffsetX;
                const startY = sourceVertex.y + sourceOffsetY;
                const endX = targetVertex.x - targetOffsetX;
                const endY = targetVertex.y - targetOffsetY;

                // Enhanced control point calculation for more pronounced curves
                const midX = (startX + endX) / 2;
                const midY = (startY + endY) / 2;
                
                // Add additional curve factor for single edges to make them slightly curved too
                const baseCurveFactor = 0;
                const controlX = midX + perpX + (baseCurveFactor * (-dy / distance));
                const controlY = midY + perpY + (baseCurveFactor * (dx / distance));

                const pathData = `M ${startX} ${startY} Q ${controlX} ${controlY} ${endX} ${endY}`;

                // Calculate the actual midpoint of the Bézier curve for label positioning
                const labelPosition = getQuadraticBezierPoint(
                    { x: startX, y: startY },
                    { x: controlX, y: controlY },
                    { x: endX, y: endY }
                );

                const transition = getTransition();

                // Calculate label dimensions
                const labelPadding = 4;
                const labelWidth = edge.label ? edge.label.length * 6 + labelPadding * 2 : 0;
                const labelHeight = 18;

                return (
                    <g key={edge.id}>
                        <motion.path
                            d={pathData}
                            stroke="#594b42"
                            strokeWidth={2}
                            fill="none"
                            initial={false}
                            animate={{ d: pathData }}
                            transition={transition}
                        />
                        {edge.label && (
                            <g>
                                <motion.rect
                                    width={labelWidth}
                                    height={labelHeight}
                                    fill="rgba(255, 255, 255, 0.95)"
                                    stroke="#594b42"
                                    strokeWidth={1}
                                    rx={3}
                                    initial={false}
                                    animate={{
                                        x: labelPosition.x - labelWidth / 2,
                                        y: labelPosition.y - labelHeight / 2
                                    }}
                                    transition={transition}
                                />
                                <motion.text
                                    textAnchor="middle"
                                    dominantBaseline="central"
                                    fill="#594b42"
                                    fontSize="11"
                                    fontWeight="600"
                                    style={{ 
                                        userSelect: 'none',
                                        fontFamily: 'system-ui, -apple-system, sans-serif'
                                    }}
                                    initial={false}
                                    animate={{
                                        x: labelPosition.x,
                                        y: labelPosition.y
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