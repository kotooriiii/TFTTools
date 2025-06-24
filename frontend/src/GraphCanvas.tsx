import React, {useEffect, useRef, useState} from 'react';
import {AnimatePresence, motion} from 'framer-motion';

const CIRCLE_RADIUS = 20;

type Vertex = {
    id: number;
    x: number;
    y: number;
    unitData?: Unit; // Add optional unit data to vertices
};

type Edge = {
    id: number;
    sourceId: number;
    targetId: number;
    label?: string; // Optional label for the edge
};

type SearchResult = {
    id: string;
    name: string;
    type: 'champion' | 'trait';
};

type SelectedItem = {
    id: string;
    name: string;
    type: 'champion' | 'trait';
};

const initialVertices: Vertex[] = [
    {id: 1, x: 100, y: 100},
    {id: 2, x: 300, y: 200},
];

// Mock search results for demonstration
const mockSearchResults: SearchResult[] = [
    {id: '1', name: 'Alistar', type: 'champion'},
    {id: '2', name: 'Graves', type: 'champion'},
    {id: '3', name: 'Golden_Ox', type: 'trait'},
    {id: '4', name: 'Sniper', type: 'trait'},
    {id: '5', name: 'Ahri', type: 'champion'},
    {id: '6', name: 'Academy', type: 'trait'},
    {id: '7', name: 'Anima_Squad', type: 'trait'},
];

// Add these new types to match your backend data
type Unit = {
    champion: string;
    traits: string[];
};

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

    // Search functionality state
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState<SearchResult[]>([]);
    const [selectedItems, setSelectedItems] = useState<SelectedItem[]>([]);

    // Add filtered units state
    const [filteredUnits, setFilteredUnits] = useState<Unit[]>([]);
    const [allUnits, setAllUnits] = useState<Unit[]>([]);

    // Add view toggle state
    const [unitsViewMode, setUnitsViewMode] = useState<'grid' | 'detailed'>('grid');


    // Add drag state for units
    const [draggedUnit, setDraggedUnit] = useState<Unit | null>(null);
    const [nextVertexId, setNextVertexId] = useState(3); // Track next available ID

    const svgRef = useRef<SVGSVGElement>(null);
    const searchPanelRef = useRef<HTMLDivElement>(null); // Add ref for search panel

    // Zoom limits
    const MIN_ZOOM = 0.6;
    const MAX_ZOOM = 2;

    const shouldHandleClickOutside = useRef(false);



    // Update this when search results change
    useEffect(() =>
    {
        shouldHandleClickOutside.current = searchResults.length > 0;
    }, [searchResults.length]);

    // Single event listener that checks the ref
    useEffect(() =>
    {
        const handleClickOutside = (event: MouseEvent) =>
        {
            if (shouldHandleClickOutside.current &&
                searchPanelRef.current &&
                !searchPanelRef.current.contains(event.target as Node))
            {
                setSearchResults([]);
                setSearchQuery('');
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    // Search functionality
    const handleSearchChange = (query: string) =>
    {
        setSearchQuery(query);

        if (query.trim() === '')
        {
            setSearchResults([]);
            return;
        }

        // Filter mock results based on query
        const filtered = mockSearchResults.filter(item =>
            item.name.toLowerCase().includes(query.toLowerCase())
        );
        setSearchResults(filtered);
    };

    const addSelectedItem = (item: SearchResult) =>
    {
        // Check if item is already selected
        if (selectedItems.some(selected => selected.id === item.id))
        {
            return;
        }

        setSelectedItems(prev => [...prev, item]);
        setSearchQuery('');
        setSearchResults([]);
    };

    const removeSelectedItem = (itemId: string) =>
    {
        setSelectedItems(prev => prev.filter(item => item.id !== itemId));
    };

    // Unit dragging functions
    const handleUnitDragStart = (e: React.DragEvent, unit: Unit) =>
    {
        setDraggedUnit(unit);

        // Create a drag image (optional - you can customize this)
        const dragImage = document.createElement('div');
        dragImage.innerHTML = unit.champion.toLowerCase().replace('_', ' ');
        dragImage.style.backgroundColor = '#6F5E53';
        dragImage.style.color = 'white';
        dragImage.style.padding = '8px 12px';
        dragImage.style.borderRadius = '6px';
        dragImage.style.position = 'absolute';
        dragImage.style.top = '-1000px';
        document.body.appendChild(dragImage);

        e.dataTransfer.setDragImage(dragImage, 0, 0);
        e.dataTransfer.effectAllowed = 'copy';

        // Clean up drag image after a short delay
        setTimeout(() => document.body.removeChild(dragImage), 0);
    };

    const handleCanvasDragOver = (e: React.DragEvent) =>
    {
        if (draggedUnit)
        {
            e.preventDefault();
            e.dataTransfer.dropEffect = 'copy';
        }
    };

    const handleCanvasDrop = (e: React.DragEvent) =>
    {
        if (!draggedUnit || !svgRef.current) return;

        e.preventDefault();

        const rect = svgRef.current.getBoundingClientRect();
        const canvasX = e.clientX - rect.left;
        const canvasY = e.clientY - rect.top;

        // Convert screen coordinates to SVG coordinates (accounting for zoom and pan)
        const svgX = (canvasX - panOffset.x) / zoom;
        const svgY = (canvasY - panOffset.y) / zoom;

        // Create new vertex with unit data
        const newVertex: Vertex = {
            id: nextVertexId,
            x: svgX,
            y: svgY,
            unitData: draggedUnit
        };

        setVertices(prev => [...prev, newVertex]);
        setNextVertexId(prev => prev + 1);
        setDraggedUnit(null);
    };

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

    const handleWheel = (e: React.WheelEvent) =>
    {
        e.preventDefault();

        if (!svgRef.current) return;

        const rect = svgRef.current.getBoundingClientRect();
        const mouseX = e.clientX - rect.left;
        const mouseY = e.clientY - rect.top;


        const isZoomOut = e.deltaY > 0;
        let newZoom;
        if (isZoomOut)
        {
            newZoom = Math.max(MIN_ZOOM, zoom / 1.2);

        } else
        {
            newZoom = Math.min(MAX_ZOOM, zoom * 1.2);

        }


        if (newZoom !== zoom)
        {
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

    const [dragOffset, setDragOffset] = useState<{x: number, y: number} | null>(null);

    const startDrag = (e: React.PointerEvent, id: number) => {
        e.stopPropagation();
        e.preventDefault();
        e.currentTarget.setPointerCapture(e.pointerId);

        if (!svgRef.current) return;

        const rect = svgRef.current.getBoundingClientRect();
        const mouseX = (e.clientX - rect.left - panOffset.x) / zoom;
        const mouseY = (e.clientY - rect.top - panOffset.y) / zoom;

        const vertex = vertices.find(v => v.id === id);
        if (vertex) {
            // Calculate offset from vertex center to mouse position
            setDragOffset({
                x: mouseX - vertex.x,
                y: mouseY - vertex.y
            });
        }

        setDraggingId(id);
    };

    const dragMove = (e: React.PointerEvent) => {
        if (!draggingId || !dragOffset || !svgRef.current) return;

        const rect = svgRef.current.getBoundingClientRect();
        const mouseX = (e.clientX - rect.left - panOffset.x) / zoom;
        const mouseY = (e.clientY - rect.top - panOffset.y) / zoom;

        // Position vertex accounting for original grab offset
        setVertices((prev) =>
            prev.map((v) =>
                v.id === draggingId
                    ? {
                        ...v,
                        x: mouseX - dragOffset.x,
                        y: mouseY - dragOffset.y
                    }
                    : v
            )
        );
    };

    const endDrag = (e: React.PointerEvent) => {
        // ... existing cleanup
        setDragOffset(null);
    };

    const handleMouseUp = () =>
    {
        setIsPanning(false);
        setLastPanPosition(null);
    };

    const handleZoomIn = () =>
    {
        const newZoom = Math.min(MAX_ZOOM, zoom * 1.2);
        setZoom(newZoom);
    };

    const handleZoomOut = () =>
    {
        const newZoom = Math.max(MIN_ZOOM, zoom / 1.2);
        setZoom(newZoom);
    };

    const handleResetZoom = () =>
    {
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
            const getVertexRadius = (vertexId: number) =>
            {
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
                        transition={{type: "spring", stiffness: 100000, damping: 100}}
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
                                style={{userSelect: 'none'}}
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

    // Mock units data - replace this with actual API call to your backend
    const mockUnits: Unit[] = [
        {champion: 'ALISTAR', traits: ['GOLDEN_OX', 'BRUISER']},
        {champion: 'ANNIE', traits: ['GOLDEN_OX', 'AMP']},
        {champion: 'APHELIOS', traits: ['GOLDEN_OX', 'MARKSMAN']},
        {champion: 'YUUMI', traits: ['ANIMA_SQUAD', 'AMP', 'STRATEGIST']},
        {champion: 'AURORA', traits: ['ANIMA_SQUAD', 'DYNAMO']},
        {champion: 'BRAND', traits: ['STREET_DEMON', 'TECHIE']},
        {champion: 'BRAUM', traits: ['SYNDICATE', 'VANGUARD']},
        {champion: 'JINX', traits: ['STREET_DEMON', 'MARKSMAN']},
        {champion: 'JHIN', traits: ['EXOTECH', 'MARKSMAN', 'DYNAMO']},
        // ... add more units from your UnitRegistry
    ];

    // Load units on component mount
    useEffect(() =>
    {
        // TODO: Replace with actual API call
        // fetch('/api/units').then(response => response.json()).then(setAllUnits);
        setAllUnits(mockUnits);
        setFilteredUnits(mockUnits); // Start with all units visible
    }, []);

    // Filter units based on selected items
    useEffect(() =>
    {
        if (selectedItems.length === 0)
        {
            setFilteredUnits(allUnits);
            return;
        }

        const selectedChampions = selectedItems
            .filter(item => item.type === 'champion')
            .map(item => item.name.toUpperCase());

        const selectedTraits = selectedItems
            .filter(item => item.type === 'trait')
            .map(item => item.name.toUpperCase());

        const filtered = allUnits.filter(unit =>
        {
            // If champions are selected, unit must be one of those champions
            const championMatch = selectedChampions.length === 0 ||
                selectedChampions.includes(unit.champion);

            // If traits are selected, unit must have at least one of those traits
            const traitMatch = selectedTraits.length === 0 ||
                selectedTraits.some(trait => unit.traits.includes(trait));

            // Unit must match both champion and trait criteria (AND logic)
            return championMatch && traitMatch;
        });

        setFilteredUnits(filtered);
    }, [selectedItems, allUnits]);

    return (
        <div style={{position: 'relative', width: '100%', height: '100vh'}}>
            {/* Filtered Units Display - Updated to card/grid layout for better drag UX */}
            {selectedItems.length > 0 && (
                <div style={{
                    position: 'absolute',
                    top: '20px',
                    left: '20px',
                    width: '320px',
                    maxHeight: '400px',
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    border: '2px solid #594b42',
                    borderRadius: '12px',
                    padding: '16px',
                    boxShadow: '0 4px 20px rgba(0, 0, 0, 0.15)',
                    backdropFilter: 'blur(10px)',
                    zIndex: 1000,
                    overflowY: 'auto'
                }}>
                    <h3 style={{
                        margin: '0 0 8px 0',
                        fontSize: '16px',
                        fontWeight: 'bold',
                        color: '#594b42'
                    }}>
                        Filtered Units ({filteredUnits.length})
                    </h3>

                    {/* View Toggle Buttons */}
                    <div style={{
                        display: 'flex',
                        gap: '4px',
                        backgroundColor: '#F5F5F5',
                        borderRadius: '6px',
                        padding: '2px'
                    }}>
                        <button
                            onClick={() => setUnitsViewMode('grid')}
                            style={{
                                fontSize: '10px',
                                padding: '4px 8px',
                                border: 'none',
                                borderRadius: '4px',
                                cursor: 'pointer',
                                backgroundColor: unitsViewMode === 'grid' ? '#6F5E53' : 'transparent',
                                color: unitsViewMode === 'grid' ? 'white' : '#666',
                                transition: 'all 0.2s ease'
                            }}
                            title="Grid View"
                        >
                            ⊞
                        </button>
                        <button
                            onClick={() => setUnitsViewMode('detailed')}
                            style={{
                                fontSize: '10px',
                                padding: '4px 8px',
                                border: 'none',
                                borderRadius: '4px',
                                cursor: 'pointer',
                                backgroundColor: unitsViewMode === 'detailed' ? '#6F5E53' : 'transparent',
                                color: unitsViewMode === 'detailed' ? 'white' : '#666',
                                transition: 'all 0.2s ease'
                            }}
                            title="Detailed View"
                        >
                            ☰
                        </button>
                    </div>

                    <p style={{
                        margin: '0 0 16px 0',
                        fontSize: '11px',
                        color: '#666',
                        fontStyle: 'italic'
                    }}>
                        Drag units to the graph canvas
                    </p>

                    {/* Grid View */}
                    {unitsViewMode === 'grid' && (
                        <div style={{
                            display: 'grid',
                            gridTemplateColumns: 'repeat(auto-fill, minmax(90px, 1fr))',
                            gap: '12px',
                            justifyItems: 'center'
                        }}>
                            {filteredUnits.map((unit, index) => (
                                <motion.div
                                    key={`${unit.champion}-${index}`}
                                    initial={{opacity: 0, scale: 0.8}}
                                    animate={{opacity: 1, scale: 1}}
                                    transition={{delay: index * 0.02}}
                                    draggable
                                    onDragStart={(e) => handleUnitDragStart(e, unit)}
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
                                        overflow: 'hidden', // Allow tooltip to extend outside
                                    }}
                                    whileHover={{
                                        scale: 1.05,
                                        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.15)',
                                        borderColor: '#8B7355'
                                    }}
                                    whileTap={{scale: 0.95}}
                                >
                                    {/* Champion Icon/Avatar placeholder */}
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
                                            paddingBottom : '4px',  // Use padding instead of margin

                                            color: '#6F5E53',
                                            textAlign: 'center',
                                            lineHeight: '1.3',  // Increase this significantly
                                            overflow: 'hidden',
                                            textOverflow: 'ellipsis',
                                            whiteSpace: 'nowrap',
                                            width: '100%',
                                        }}
                                        title={unit.champion.toLowerCase().replace('_', ' ')} // Full name on hover
                                    >
                                        {unit.champion.toLowerCase().replace('_', ' ')}
                                    </div>

                                    {/* Trait indicators - simplified dots with tooltip */}
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
                                                title={trait.toLowerCase().replace('_', ' ')}
                                                style={{
                                                    width: '12px', // Larger hover target
                                                    height: '12px', // Larger hover target
                                                    display: 'flex',
                                                    alignItems: 'center',
                                                    justifyContent: 'center',
                                                    cursor: 'help',
                                                    position: 'relative'
                                                }}
                                            >
                                                <div style={{
                                                    width: '8px', // Visual dot stays small
                                                    height: '8px',
                                                    borderRadius: '50%',
                                                    backgroundColor: selectedItems.some(item =>
                                                        item.type === 'trait' &&
                                                        item.name.toUpperCase() === trait
                                                    ) ? '#8B7355' : '#E5E5E5',
                                                }}/>
                                            </div>
                                        ))}
                                        {unit.traits.length > 4 && (
                                            <div
                                                style={{
                                                    fontSize: '8px',
                                                    color: '#666',
                                                    marginLeft: '2px',
                                                    cursor: 'help',
                                                    padding: '2px 4px', // Add padding for easier hovering
                                                    minWidth: '16px',
                                                    textAlign: 'center'
                                                }}
                                                title={`Additional traits: ${unit.traits.slice(4).map(trait =>
                                                    trait.toLowerCase().replace('_', ' ')
                                                ).join(', ')}`}
                                            >
                                                +{unit.traits.length - 4}
                                            </div>
                                        )}
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

                                    {/* Enhanced tooltip for full unit info on hover */}
                                    <div
                                        style={{
                                            position: 'absolute',
                                            bottom: '-60px',
                                            left: '50%',
                                            transform: 'translateX(-50%)',
                                            backgroundColor: 'rgba(0, 0, 0, 0.9)',
                                            color: 'white',
                                            padding: '8px 10px',
                                            borderRadius: '6px',
                                            fontSize: '10px',
                                            whiteSpace: 'nowrap',
                                            zIndex: 1001,
                                            opacity: 0,
                                            pointerEvents: 'none',
                                            transition: 'opacity 0.2s ease',
                                            maxWidth: '200px',
                                            textAlign: 'center'
                                        }}
                                        className="unit-tooltip"
                                    >
                                        <div style={{fontWeight: 'bold', marginBottom: '4px'}}>
                                            {unit.champion.toLowerCase().replace('_', ' ')}
                                        </div>
                                        <div style={{fontSize: '9px'}}>
                                            Traits: {unit.traits.map(trait =>
                                            trait.toLowerCase().replace('_', ' ')
                                        ).join(', ')}
                                        </div>
                                        <div style={{
                                            position: 'absolute',
                                            top: '-4px',
                                            left: '50%',
                                            transform: 'translateX(-50%)',
                                            width: 0,
                                            height: 0,
                                            borderLeft: '4px solid transparent',
                                            borderRight: '4px solid transparent',
                                            borderBottom: '4px solid rgba(0, 0, 0, 0.9)'
                                        }}/>
                                    </div>
                                </motion.div>
                            ))}
                        </div>
                    )}

                    {/* Detailed View */}
                    {unitsViewMode === 'detailed' && (
                        <div style={{display: 'flex', flexDirection: 'column', gap: '8px'}}>
                            {filteredUnits.map((unit, index) => (
                                <motion.div
                                    key={`${unit.champion}-${index}-detailed`}
                                    initial={{opacity: 0, x: -20}}
                                    animate={{opacity: 1, x: 0}}
                                    transition={{delay: index * 0.03}}
                                    draggable
                                    onDragStart={(e) => handleUnitDragStart(e, unit)}
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
                                    whileTap={{scale: 0.98}}
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
                                        {/* Drag handle */}
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
                                    <div style={{flex: 1, minWidth: 0}}>
                                        {/* Champion Name */}
                                        <div style={{
                                            fontSize: '14px',
                                            fontWeight: 'bold',
                                            color: '#6F5E53',
                                            marginBottom: '6px'
                                        }}>
                                            {unit.champion.toLowerCase().replace('_', ' ')}
                                        </div>

                                        {/* Traits - Full list with labels */}
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

                                        {/* Additional info */}
                                        <div style={{
                                            fontSize: '9px',
                                            color: '#888',
                                            fontStyle: 'italic'
                                        }}>
                                            {unit.traits.length} trait{unit.traits.length !== 1 ? 's' : ''}
                                        </div>
                                    </div>

                                    {/* Quick action indicator */}
                                    <div style={{
                                        fontSize: '12px',
                                        color: '#999',
                                        display: 'flex',
                                        flexDirection: 'column',
                                        alignItems: 'center',
                                        gap: '2px'
                                    }}>
                                        <div>→</div>
                                        <div style={{fontSize: '8px'}}>drag</div>
                                    </div>
                                </motion.div>
                            ))}
                        </div>
                    )}
                </div>
            )}


            {/* Search Panel - Bottom Middle */}
            <div style={{
                position: 'absolute',
                bottom: '20px',
                left: '0',
                right: '0',
                display: 'flex',
                justifyContent: 'center',
                zIndex: 1000,
                pointerEvents: 'none'
            }}>
                <motion.div
                    ref={searchPanelRef} // Add ref here
                    initial={{opacity: 0, y: 50}}
                    animate={{opacity: 1, y: 0}}
                    style={{
                        backgroundColor: 'rgba(255, 255, 255, 0.95)',
                        border: '2px solid #594b42',
                        borderRadius: '12px',
                        padding: '20px',
                        width: '500px',
                        maxWidth: '90vw',
                        maxHeight: '400px',
                        boxShadow: '0 -4px 20px rgba(0, 0, 0, 0.15)',
                        backdropFilter: 'blur(10px)',
                        pointerEvents: 'auto'
                    }}
                >
                    {/* Search Input */}
                    <div style={{marginBottom: '16px'}}>
                        <input
                            type="text"
                            placeholder="Search champions or traits..."
                            value={searchQuery}
                            onChange={(e) => handleSearchChange(e.target.value)}
                            style={{
                                width: '100%',
                                padding: '12px',
                                border: '2px solid #C3A995',
                                borderRadius: '8px',
                                fontSize: '16px',
                                outline: 'none',
                                transition: 'border-color 0.2s ease'
                            }}
                            onFocus={(e) => e.target.style.borderColor = '#6F5E53'}
                            onBlur={(e) => e.target.style.borderColor = '#C3A995'}
                        />
                    </div>

                    {/* Selected Items (Cards) */}
                    {selectedItems.length > 0 && (
                        <div style={{marginBottom: '16px'}}>
                            <div style={{
                                display: 'flex',
                                flexWrap: 'wrap',
                                gap: '8px'
                            }}>
                                <AnimatePresence>
                                    {selectedItems.map((item) => (
                                        <motion.div
                                            key={item.id}
                                            initial={{opacity: 0, scale: 0.8}}
                                            animate={{opacity: 1, scale: 1}}
                                            exit={{opacity: 0, scale: 0.8}}
                                            style={{
                                                backgroundColor: item.type === 'champion' ? '#6F5E53' : '#8B7355',
                                                color: 'white',
                                                padding: '6px 12px',
                                                borderRadius: '20px',
                                                fontSize: '12px',
                                                fontWeight: 'bold',
                                                display: 'flex',
                                                alignItems: 'center',
                                                gap: '6px'
                                            }}
                                        >
                                            <span>{item.name}</span>
                                            <button
                                                onClick={() => removeSelectedItem(item.id)}
                                                style={{
                                                    background: 'none',
                                                    border: 'none',
                                                    color: 'white',
                                                    cursor: 'pointer',
                                                    fontSize: '14px',
                                                    fontWeight: 'bold',
                                                    padding: '0',
                                                    width: '16px',
                                                    height: '16px',
                                                    borderRadius: '50%',
                                                    display: 'flex',
                                                    alignItems: 'center',
                                                    justifyContent: 'center'
                                                }}
                                                title="Remove"
                                            >
                                                ×
                                            </button>
                                        </motion.div>
                                    ))}
                                </AnimatePresence>
                            </div>
                        </div>
                    )}

                    {/* Search Results - Grid Format with Icons */}
                    {searchResults.length > 0 && (
                        <div style={{
                            maxHeight: '250px',
                            overflowY: 'auto',
                            border: '1px solid #C3A995',
                            borderRadius: '8px',
                            backgroundColor: '#F5F5F5',
                            padding: '8px'
                        }}>
                            <div style={{
                                display: 'grid',
                                gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))',
                                gap: '8px'
                            }}>
                                <AnimatePresence>
                                    {searchResults.map((result) => (
                                        <motion.div
                                            key={result.id}
                                            initial={{opacity: 0, scale: 0.9}}
                                            animate={{opacity: 1, scale: 1}}
                                            exit={{opacity: 0, scale: 0.9}}
                                            onClick={() => addSelectedItem(result)}
                                            style={{
                                                padding: '12px',
                                                border: '2px solid transparent',
                                                borderRadius: '8px',
                                                cursor: 'pointer',
                                                display: 'flex',
                                                alignItems: 'center',
                                                gap: '12px',
                                                backgroundColor: 'white',
                                                transition: 'all 0.2s ease',
                                                boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)'
                                            }}
                                            onMouseEnter={(e) =>
                                            {
                                                e.currentTarget.style.borderColor = result.type === 'champion' ? '#6F5E53' : '#8B7355';
                                                e.currentTarget.style.transform = 'translateY(-2px)';
                                                e.currentTarget.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.15)';
                                            }}
                                            onMouseLeave={(e) =>
                                            {
                                                e.currentTarget.style.borderColor = 'transparent';
                                                e.currentTarget.style.transform = 'translateY(0px)';
                                                e.currentTarget.style.boxShadow = '0 2px 4px rgba(0, 0, 0, 0.1)';
                                            }}
                                        >
                                            {/* Icon Container */}
                                            <div style={{
                                                width: '40px',
                                                height: '40px',
                                                borderRadius: result.type === 'champion' ? '8px' : '50%',
                                                backgroundColor: result.type === 'champion' ? '#6F5E53' : '#8B7355',
                                                display: 'flex',
                                                alignItems: 'center',
                                                justifyContent: 'center',
                                                color: 'white',
                                                fontSize: '18px',
                                                fontWeight: 'bold',
                                                flexShrink: 0,
                                                border: '2px solid rgba(255, 255, 255, 0.3)'
                                            }}>
                                                {/* Placeholder for actual icons - you can replace with <img> tags */}
                                                {result.type === 'champion' ? '⚔️' : '✨'}
                                            </div>

                                            {/* Text Content */}
                                            <div style={{flex: 1, minWidth: 0}}>
                                                <div style={{
                                                    fontSize: '14px',
                                                    fontWeight: 'bold',
                                                    color: '#594b42',
                                                    marginBottom: '2px'
                                                }}>
                                                    {result.name}
                                                </div>
                                                <div style={{
                                                    fontSize: '11px',
                                                    color: '#888',
                                                    backgroundColor: result.type === 'champion' ? '#E8F4FD' : '#FFF2E8',
                                                    padding: '2px 6px',
                                                    borderRadius: '10px',
                                                    display: 'inline-block'
                                                }}>
                                                    {result.type}
                                                </div>
                                            </div>
                                        </motion.div>
                                    ))}
                                </AnimatePresence>
                            </div>
                        </div>
                    )}

                    {/* No Results Message */}
                    {searchQuery && searchResults.length === 0 && (
                        <div style={{
                            textAlign: 'center',
                            padding: '20px',
                            color: '#888',
                            fontSize: '14px'
                        }}>
                            No results found for "{searchQuery}"
                        </div>
                    )}
                </motion.div>
            </div>

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

            {/* Zoom Level Display - Moved to bottom left to avoid conflict */}
            <div style={{
                position: 'absolute',
                bottom: '20px',
                left: '20px',
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
                onDragOver={handleCanvasDragOver}
                onDrop={handleCanvasDrop}
            >
                <g transform={`translate(${panOffset.x}, ${panOffset.y}) scale(${zoom})`}>
                    {/* Render edges first so they appear behind vertices */}
                    {renderEdges()}

                    {/* Updated vertex rendering with unit data support */}
                    {vertices.map((v) => (
                        <g key={v.id}>
                            <motion.circle
                                cx={v.x}
                                cy={v.y}
                                r={CIRCLE_RADIUS}
                                fill={v.unitData ? "#8B7355" : "#6F5E53"}
                                stroke="#594b42"
                                strokeWidth={1.5}
                                style={{cursor: 'move'}}
                                onMouseEnter={() => setHoveringId(v.id)}
                                onMouseLeave={() => setHoveringId(null)}
                                onPointerDown={(e) => startDrag(e, v.id)}
                                onPointerMove={dragMove}
                                onPointerUp={endDrag}
                                animate={{
                                    scale: draggingId === v.id ? 0.9 : (hoveringId === v.id ? 1.1 : 1)
                                }}
                            />

                            {/* Add unit label if vertex has unit data */}
                            {v.unitData && (
                                <motion.text
                                    x={v.x}
                                    y={v.y + CIRCLE_RADIUS + 15}
                                    textAnchor="middle"
                                    dominantBaseline="middle"
                                    fill="#594b42"
                                    fontSize="10"
                                    fontWeight="bold"
                                    style={{userSelect: 'none', pointerEvents: 'none'}}
                                    animate={{
                                        scale: draggingId === v.id ? 0.9 : (hoveringId === v.id ? 1.1 : 1)
                                    }}
                                >
                                    {v.unitData.champion.toLowerCase().replace('_', ' ')}
                                </motion.text>
                            )}
                        </g>
                    ))}
                </g>
            </svg>
        </div>
    );
};

export default GraphCanvas;