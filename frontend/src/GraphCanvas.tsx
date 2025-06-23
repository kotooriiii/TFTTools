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
    const [edges, setEdges] = useState<Edge[]>([
        {id: 1, sourceId: 1, targetId: 2, label: "connection"}
    ]);

    const [isPanning, setIsPanning] = useState(false);
    const [panOffset, setPanOffset] = useState({x: 0, y: 0});
    const [lastPanPosition, setLastPanPosition] = useState<{ x: number; y: number } | null>(null);
    const svgRef = useRef<SVGSVGElement>(null);

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

    const handleVertexDrag = (vertexId: number, event: any, info: PanInfo) =>
    {
        // Update the actual vertices state

        setVertices(prev => prev.map(vertex =>
            vertex.id === vertexId
                ? {...vertex, x: vertex.x + info.delta.x, y: vertex.y + info.delta.y}
                : vertex
        ));

    };

    const handleVertexDragEnd = (vertexId: number, event: any, info: PanInfo) =>
    {

    };

    const handleMouseUp = () =>
    {
        setIsPanning(false);
        setLastPanPosition(null);
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
            const textY = midY; // Move text 20 pixels below the midpoint


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
                                x={textX - (edge.label.length * 3.5)} // Approximate width based on text
                                y={textY - 8}
                                width={edge.label.length * 7}
                                height={16}
                                fill="white"
                                stroke="#594b42"
                                strokeWidth={1}
                                rx={2} // Rounded corners
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
                                style={{ userSelect: 'none' }} // Makes text non-selectable
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
        <svg
            ref={svgRef}
            width="100%"
            height="100vh"
            style={{background: '#C3A995', cursor: isPanning ? 'grabbing' : 'grab'}}
            onMouseDown={handleMouseDown}
            onMouseMove={handleMouseMove}
            onMouseUp={handleMouseUp}
            onMouseLeave={handleMouseUp}
        >
            <g transform={`translate(${panOffset.x}, ${panOffset.y})`}>
                {/* Render edges first so they appear behind vertices */}
                {renderEdges()}

                {/* Your existing vertex rendering */}
                {vertices.map((v) => (
                    <motion.circle
                        key={v.id}
                        initial={{x: v.x, y: v.y}} // Set initial position
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
    );
};

export default GraphCanvas;