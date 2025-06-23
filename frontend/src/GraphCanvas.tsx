import React, {useState, useRef} from 'react';
import {motion, PanInfo} from 'framer-motion';

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

    const handleVertexDrag = (vertexId: number, _event: MouseEvent | TouchEvent | PointerEvent, info: PanInfo) =>
    {
        // Update the actual vertices state
        setVertices(prev => prev.map(vertex =>
            vertex.id === vertexId
                ? {...vertex, x: vertex.x + info.delta.x / zoom, y: vertex.y + info.delta.y / zoom}
                : vertex
        ));
    };

    const handleVertexDragEnd = (_vertexId: number, _event: MouseEvent | TouchEvent | PointerEvent, _info: PanInfo) =>
    {
        // Handle drag end logic if needed
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

            // Calculate edge endpoints considering vertex radius
            const dx = targetVertex.x - sourceVertex.x;
            const dy = targetVertex.y - sourceVertex.y;
            const distance = Math.sqrt(dx * dx + dy * dy);

            // Offset by vertex radius (20) to connect at circle edge, not center
            const offsetX = (dx / distance) * 20;
            const offsetY = (dy / distance) * 20;

            const startX = sourceVertex.x + offsetX;
            const startY = sourceVertex.y + offsetY;
            const endX = targetVertex.x - offsetX;
            const endY = targetVertex.y - offsetY;

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
                        transition={{ type: "spring", stiffness: 5000, damping: 100 }}
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
                            initial={{x: v.x, y: v.y}}
                            r={20}
                            fill="#6F5E53"
                            stroke="#594b42"
                            strokeWidth={1.5}
                            drag
                            dragMomentum={false}
                            dragElastic={0}
                            onDrag={(event, info) => handleVertexDrag(v.id, event, info)}
                            onDragEnd={(event, info) => handleVertexDragEnd(v.id, event, info)}
                            style={{cursor: 'move'}}
                            whileHover={{scale: 1.1}}
                            whileTap={{scale: 0.95}}
                            whileDrag={{scale: 1.05}}
                        />
                    ))}
                </g>
            </svg>
        </div>
    );
};

export default GraphCanvas;