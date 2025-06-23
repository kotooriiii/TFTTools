import React, {useState, useRef} from 'react';
import {motion} from 'framer-motion';

const CIRCLE_RADIUS = 20;

type Vertex = {
    id: number;
    x: number;
    y: number;
};
type Edge = {
    id: number;
    sourceId: number;
    targetId: number;
    label?: string; // Optional label for the edge
};

const initialVertices: Vertex[] = [
    {id: 1, x: 100, y: 100},
    {id: 2, x: 300, y: 200},
];

const GraphCanvas: React.FC = () =>
{
    const [vertices, setVertices] = useState(initialVertices);
    const [edges] = useState<Edge[]>([
        {id: 1, sourceId: 1, targetId: 2, label: "connection"}
    ]);

    const [isPanning, setIsPanning] = useState(false);
    const [panOffset, setPanOffset] = useState({x: 0, y: 0});
    const [lastPanPosition, setLastPanPosition] = useState<{ x: number; y: number } | null>(null);
    const [zoom, setZoom] = useState(1);
    const [draggingId, setDraggingId] = useState<number | null>(null);
    const [hoveringId, setHoveringId] = useState<number | null>(null);
    const [lastPointerPos, setLastPointerPos] = useState<{ x: number; y: number } | null>(null);

    const svgRef = useRef<SVGSVGElement>(null);

    // Zoom limits
    const MIN_ZOOM = 0.6;
    const MAX_ZOOM = 2;
    const ZOOM_STEP = 0.2;

    const handleMouseDown = (e: React.MouseEvent) =>
    {
        if (e.button !== 0 || e.target !== e.currentTarget) return;
        setIsPanning(true);
        setLastPanPosition({x: e.clientX, y: e.clientY});
    };

    const handleMouseMove = (e: React.MouseEvent) =>
    {
        if (isPanning && lastPanPosition)
        {
            const dx = e.clientX - lastPanPosition.x;
            const dy = e.clientY - lastPanPosition.y;

            // Update pan offset for visual feedback
            setPanOffset((prev) => ({x: prev.x + dx, y: prev.y + dy}));

            setLastPanPosition({x: e.clientX, y: e.clientY});
        }
    };

    const handleWheel = (e: React.WheelEvent) => {
        e.preventDefault();
        
        if (!svgRef.current) return;

        const rect = svgRef.current.getBoundingClientRect();
        const mouseX = e.clientX - rect.left;
        const mouseY = e.clientY - rect.top;

        // Calculate zoom change - add/subtract 20 percentage points
        const zoomChange = e.deltaY > 0 ? -ZOOM_STEP : ZOOM_STEP;
        const newZoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, zoom + zoomChange));


        if (newZoom !== zoom) {
            // Calculate the point to zoom into (mouse position relative to current transform)
            const zoomPointX = (mouseX - panOffset.x) / zoom;
            const zoomPointY = (mouseY - panOffset.y) / zoom;

            // Update pan offset to keep zoom centered on mouse position
            const newPanX = mouseX - zoomPointX * newZoom;
            const newPanY = mouseY - zoomPointY * newZoom;

            setZoom(newZoom);
            setPanOffset({x: newPanX, y: newPanY});
        }
    };
    const startDrag = (e: React.PointerEvent, id: number) => {
        e.stopPropagation();
        e.preventDefault();
        e.currentTarget.setPointerCapture(e.pointerId);

        setDraggingId(id);
        setLastPointerPos({ x: e.clientX, y: e.clientY });
    };

    const dragMove = (e: React.PointerEvent) => {
        if (!draggingId || !lastPointerPos) return;

        const dx = (e.clientX - lastPointerPos.x) / zoom;
        const dy = (e.clientY - lastPointerPos.y) / zoom;

        setVertices((prev) =>
            prev.map((v) =>
                v.id == draggingId
                    ? { ...v, x: v.x + dx, y: v.y + dy }
                    : v
            )
        );
        setLastPointerPos({ x: e.clientX, y: e.clientY });
    };

    const endDrag = (e: React.PointerEvent) => {
        e.stopPropagation();
        e.preventDefault();
        e.currentTarget.releasePointerCapture(e.pointerId);

        setDraggingId(null);

        setLastPointerPos(null);
    };

    const handleMouseUp = () =>
    {
        setIsPanning(false);
        setLastPanPosition(null);
    };

    const handleZoomIn = () => {
        const newZoom = Math.min(MAX_ZOOM, zoom * 1.2);
        setZoom(newZoom);
    };

    const handleZoomOut = () => {
        const newZoom = Math.max(MIN_ZOOM, zoom / 1.2);
        setZoom(newZoom);
    };

    const handleResetZoom = () => {
        setZoom(1);
        setPanOffset({x: 0, y: 0});
    };

    const renderEdges = () =>
    {
        return edges.map((edge) =>
        {
            const sourceVertex = vertices.find(v => v.id === edge.sourceId);
            const targetVertex = vertices.find(v => v.id === edge.targetId);

            if (!sourceVertex || !targetVertex) return null;

            // Calculate dynamic radius based on current scale
            const getVertexRadius = (vertexId: number) => {
                const baseRadius = CIRCLE_RADIUS;
                if (draggingId === vertexId) return baseRadius * 0.9;
                if (hoveringId === vertexId) return baseRadius * 1.1;
                return baseRadius;
            };
            const sourceRadius = getVertexRadius(sourceVertex.id);
            const targetRadius = getVertexRadius(targetVertex.id);

            // Calculate edge endpoints considering vertex radius
            const dx = targetVertex.x - sourceVertex.x;
            const dy = targetVertex.y - sourceVertex.y;
            const distance = Math.sqrt(dx * dx + dy * dy);

            const sourceOffsetX = (dx / distance) * sourceRadius;
            const sourceOffsetY = (dy / distance) * sourceRadius;
            const targetOffsetX = (dx / distance) * targetRadius;
            const targetOffsetY = (dy / distance) * targetRadius;


            const startX = sourceVertex.x + sourceOffsetX;
            const startY = sourceVertex.y + sourceOffsetY;
            const endX = targetVertex.x - targetOffsetX;
            const endY = targetVertex.y - targetOffsetY;

            // Calculate text position - midpoint of the line
            const midX = (startX + endX) / 2;
            const midY = (startY + endY) / 2;

            const textX = midX;
            const textY = midY;

            return (
                <g key={edge.id}>
                    <motion.line
                        key={edge.id}
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
                        transition={{ type: "spring", stiffness: 100000, damping: 100 }}
                    />
                    {edge.label && (
                        <g>
                            {/* Background rectangle for better readability */}
                            <motion.rect
                                x={textX - (edge.label.length * 3.5)}
                                y={textY - 8}
                                width={edge.label.length * 7}
                                height={16}
                                fill="white"
                                stroke="#594b42"
                                strokeWidth={1}
                                rx={2}
                                initial={false}
                                transition={{type: "spring", stiffness: 300, damping: 30}}
                            />
                            <motion.text
                                x={textX}
                                y={textY}
                                textAnchor="middle"
                                dominantBaseline="middle"
                                fill="#594b42"
                                fontSize="12"
                                fontWeight="bold"
                                style={{ userSelect: 'none' }}
                                initial={false}
                                transition={{type: "spring", stiffness: 300, damping: 30}}
                            >
                                {edge.label}
                            </motion.text>
                        </g>
                    )}
                </g>
            );
        });
    };

    return (
        <div style={{ position: 'relative', width: '100%', height: '100vh' }}>
            {/* Zoom Controls */}
            <div style={{
                position: 'absolute',
                top: '20px',
                right: '20px',
                zIndex: 1000,
                display: 'flex',
                flexDirection: 'column',
                gap: '8px'
            }}>
                <button 
                    onClick={handleZoomIn}
                    style={{
                        padding: '8px 12px',
                        backgroundColor: '#6F5E53',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        fontSize: '16px',
                        fontWeight: 'bold'
                    }}
                    title="Zoom In"
                >
                    +
                </button>
                <button 
                    onClick={handleZoomOut}
                    style={{
                        padding: '8px 12px',
                        backgroundColor: '#6F5E53',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        fontSize: '16px',
                        fontWeight: 'bold'
                    }}
                    title="Zoom Out"
                >
                    −
                </button>
                <button 
                    onClick={handleResetZoom}
                    style={{
                        padding: '8px 12px',
                        backgroundColor: '#594b42',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        fontSize: '12px'
                    }}
                    title="Reset Zoom"
                >
                    Reset
                </button>
            </div>

            {/* Zoom Level Display */}
            <div style={{
                position: 'absolute',
                bottom: '20px',
                right: '20px',
                zIndex: 1000,
                backgroundColor: 'rgba(255, 255, 255, 0.9)',
                padding: '8px 12px',
                borderRadius: '4px',
                fontSize: '14px',
                fontWeight: 'bold',
                color: '#594b42'
            }}>
                {Math.round(zoom * 100)}%
            </div>

            <svg
                ref={svgRef}
                width="100%"
                height="100vh"
                style={{background: '#C3A995', cursor: isPanning ? 'grabbing' : 'grab'}}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                onMouseLeave={handleMouseUp}
                onWheel={handleWheel}
            >
                <g transform={`translate(${panOffset.x}, ${panOffset.y}) scale(${zoom})`}>
                    {/* Render edges first so they appear behind vertices */}
                    {renderEdges()}

                    {/* Your existing vertex rendering */}
                    {vertices.map((v) => (
                        <motion.circle
                            key={v.id}
                            cx={v.x}
                            cy={v.y}
                            r={CIRCLE_RADIUS}
                            fill="#6F5E53"
                            stroke="#594b42"
                            strokeWidth={1.5}
                            style={{ cursor: 'move' }}
                            onMouseEnter={() => setHoveringId(v.id)}
                            onMouseLeave={() => setHoveringId(null)}
                            onPointerDown={(e) => startDrag(e, v.id)}
                            onPointerMove={dragMove}
                            onPointerUp={endDrag}
                            animate={{
                                scale: draggingId === v.id ? 0.9 : (hoveringId === v.id ? 1.1 : 1)
                            }}
                        />

                    ))}
                </g>
            </svg>
        </div>
    );
};

export default GraphCanvas;