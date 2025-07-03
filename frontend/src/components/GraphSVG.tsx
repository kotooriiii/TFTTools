import React, {forwardRef} from 'react';
import {Edge, Vertex} from '../types/graphTypes';
import {Unit} from '../types/unitTypes';
import {GraphVertex} from './GraphVertex';
import {GraphEdges} from './GraphEdges';
import {motion} from 'framer-motion';

interface GraphSVGProps
{
    vertices: Vertex[];
    setVertices: React.Dispatch<React.SetStateAction<Vertex[]>>;
    edges: Edge[];
    onCanvasDrop: (e: React.DragEvent) => void;
    onCanvasDragOver: (e: React.DragEvent) => void;
    draggedUnit: Unit | null;
    isPanning: boolean;
    panOffset: { x: number; y: number };
    panVelocity: { x: number; y: number };
    zoom: number;
    draggingId: number | null;
    hoveringId: number | null;
    setHoveringId: (id: number | null) => void;
    handleMouseDown: (e: React.MouseEvent) => void;
    handleMouseMove: (e: React.MouseEvent) => void;
    handleMouseUp: () => void;
    handleWheel: (e: React.WheelEvent) => void;
    startDrag: (e: React.PointerEvent, id: number, vertices: Vertex[]) => void;
    dragMove: (e: React.PointerEvent, setVertices: React.Dispatch<React.SetStateAction<Vertex[]>>) => void;
    endDrag: () => void;
    mousePosition: { x: number; y: number } | null;
    highlightedUnitId: number | null;
}

export const GraphSVG = forwardRef<SVGSVGElement, GraphSVGProps>(({
                                                                      vertices,
                                                                      setVertices,
                                                                      edges,
                                                                      onCanvasDrop,
                                                                      onCanvasDragOver,
                                                                      draggedUnit,
                                                                      isPanning,
                                                                      panOffset,
                                                                      panVelocity,
                                                                      zoom,
                                                                      draggingId,
                                                                      hoveringId,
                                                                      setHoveringId,
                                                                      handleMouseDown,
                                                                      handleMouseMove,
                                                                      handleMouseUp,
                                                                      handleWheel,
                                                                      startDrag,
                                                                      dragMove,
                                                                      endDrag,
                                                                      mousePosition,
                                                                      highlightedUnitId,
                                                                  }, ref) =>
{
    return (
        <div style={{
            position: 'absolute',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            backgroundColor: '#C3A995',
            overflow: 'hidden'
        }}>
            <svg
                ref={ref}
                width="100%"
                height="100%"
                style={{
                    cursor: isPanning ? 'grabbing' : 'grab',
                    display: 'block'
                }}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                onWheel={handleWheel}
                onDrop={onCanvasDrop}
                onDragOver={onCanvasDragOver}
            >
                <motion.g
                    initial={false}
                    style={{
                        originY: '0',
                        originX: '0',
                    }}
                    animate={{
                        x: panOffset.x,
                        y: panOffset.y,
                        scale: zoom
                    }}
                    transition={{
                        x: {
                            type: "tween",
                            duration: 0.2,
                            ease: "easeOut",
                            velocity: panVelocity.x
                        },
                        y: {
                            type: "tween",
                            duration: 0.2,
                            ease: "easeOut",
                            velocity: panVelocity.y
                        },
                        scale: {
                            type: "spring",
                            stiffness: 200,
                            damping: 20,
                            mass: 0.9
                        }
                    }}
                >
                    <GraphEdges
                        edges={edges}
                        vertices={vertices}
                        draggingId={draggingId}
                        hoveringId={hoveringId}
                    />

                    {/* Render vertices */}
                    {vertices.map((vertex) => (
                        <GraphVertex
                            key={vertex.id}
                            vertex={vertex}
                            highlightedUnitId={highlightedUnitId}
                            isDragging={draggingId === vertex.id}
                            isHovering={hoveringId === vertex.id}
                            onPointerEnter={() => setHoveringId(vertex.id)}
                            onPointerLeave={() => setHoveringId(null)}
                            onPointerDown={(e) => startDrag(e, vertex.id, vertices)}
                            onPointerMove={(e) => dragMove(e, setVertices)}
                            onPointerUp={endDrag}
                            mousePosition={mousePosition}
                        />
                    ))}
                </motion.g>
            </svg>
        </div>
    )
        ;
});

GraphSVG.displayName = 'GraphSVG';