import React, {useRef, useState} from 'react';
import {useGraphInteractions} from './hooks/useGraphInteractions';
import {useSearch} from './hooks/useSearch';
import {useUnitFiltering} from './hooks/useUnitFiltering';
import {useDragAndDrop} from './hooks/useDragAndDrop.ts';
import {GraphSVG} from './components/GraphSVG';
import {SearchPanel} from './components/SearchPanel';
import {UnitsPanel} from './components/UnitsPanel';
import {ZoomControls} from './components/ZoomControls';
import {Edge, Vertex} from "./types/graphTypes.ts";
import {Unit} from "./types/unitTypes.ts";

const GraphCanvas: React.FC = () =>
{
    const svgRef = useRef<SVGSVGElement>(null);
    const searchPanelRef = useRef<HTMLDivElement>(null);

    const [vertices, setVertices] = useState<Vertex[]>([]);
    const [edges, setEdges] = useState<Edge[]>([]);

    const [nextVertexId, setNextVertexId] = useState(1);
    const [nextEdgeId, setNextEdgeId] = useState(1);


    const graphInteractions = useGraphInteractions(svgRef);
    const search = useSearch(searchPanelRef);
    const unitFiltering = useUnitFiltering(search.selectedItems);
    const dragAndDrop = useDragAndDrop(svgRef, graphInteractions.panOffset, graphInteractions.zoom);

    const [highlightedUnitId, setHighlightedUnitId] = useState<number | null>(null);

    // Helper function to find shared traits between two units
    const findSharedTraits = (unit1: Unit, unit2: Unit): string[] => {
        if (!unit1?.traits || !unit2?.traits) return [];
        return unit1.traits.filter((trait: string) => unit2.traits.includes(trait));
    };

    // Helper function to create edges between vertices with shared traits
    const createEdgesForNewVertex = (newVertex: Vertex, existingVertices: Vertex[]): Edge[] => {
        const newEdges: Edge[] = [];

        if (!newVertex.unitData) return newEdges;

        for (const existingVertex of existingVertices) {
            if (!existingVertex.unitData || existingVertex.id === newVertex.id) continue;

            const sharedTraits = findSharedTraits(newVertex.unitData, existingVertex.unitData);

            if (sharedTraits.length > 0) {
                // Create one edge per shared trait
                for (const trait of sharedTraits) {
                    const newEdge: Edge = {
                        id: nextEdgeId + newEdges.length,
                        sourceId: existingVertex.id,
                        targetId: newVertex.id,
                        label: trait
                    };
                    newEdges.push(newEdge);
                }
            }
        }

        return newEdges;
    };



    const handleCanvasDrop = (e: React.DragEvent) =>
    {
        if (!dragAndDrop.draggedUnit || !svgRef.current) return;

        e.preventDefault();

        // Check if unit already exists on canvas
        const existingVertex = vertices.find(vertex => 
            vertex.unitData?.champion === dragAndDrop.draggedUnit?.champion
        );

        if (existingVertex) {
            // Pan to existing unit instead of creating a new one
            graphInteractions.panToUnit(existingVertex.x, existingVertex.y);

            setHighlightedUnitId(existingVertex.id);
            // Clear highlight after a short duration
            setTimeout(() => {
                setHighlightedUnitId(null);
            }, 1500);
            dragAndDrop.setDraggedUnit(null);
            return;
        }

        const motionG = svgRef.current.querySelector('g') as SVGGElement;
        const ctm = motionG.getScreenCTM();
        const point = svgRef.current.createSVGPoint();

        point.x = e.clientX;
        point.y = e.clientY;

        const svgPoint = point.matrixTransform(ctm.inverse());

        const newVertex = {
            id: nextVertexId,
            x: svgPoint.x,
            y: svgPoint.y,
            unitData: dragAndDrop.draggedUnit
        };

        // Create edges between the new vertex and existing vertices with shared traits
        const newEdges = createEdgesForNewVertex(newVertex, vertices);


        setVertices(prev => [...prev, newVertex]);
        setEdges(prev => [...prev, ...newEdges]);
        setNextVertexId(prev => prev + 1);
        setNextEdgeId(prev => prev + newEdges.length);
        dragAndDrop.setDraggedUnit(null);
        return;


    };


    return (
        <div style={{position: 'relative', width: '100%', height: '100vh'}}>
            {search.selectedItems.length > 0 && (
                <UnitsPanel
                    filteredUnits={unitFiltering.filteredUnits}
                    onUnitDragStart={dragAndDrop.handleUnitDragStart}
                    selectedItems={search.selectedItems}
                    isLoading={unitFiltering.isLoading}
                />
            )}

            <SearchPanel
                ref={searchPanelRef}
                searchQuery={search.searchQuery}
                searchResults={search.searchResults}
                selectedItems={search.selectedItems}
                onSearchChange={search.handleSearchChange}
                isLoading={search.isLoading}
                onAddSelectedItem={search.addSelectedItem}
                onRemoveSelectedItem={search.removeSelectedItem}
            />

            <GraphSVG
                ref={svgRef}
                vertices={vertices}
                setVertices={setVertices}
                edges={edges}
                onCanvasDrop={handleCanvasDrop}
                onCanvasDragOver={dragAndDrop.handleCanvasDragOver}
                draggedUnit={dragAndDrop.draggedUnit}
                highlightedUnitId={highlightedUnitId}
                {...graphInteractions}
            />

            <ZoomControls
                onZoomIn={graphInteractions.handleZoomIn}
                onZoomOut={graphInteractions.handleZoomOut}
                onResetZoom={graphInteractions.handleResetZoom}
                zoom={graphInteractions.zoom}
            />
        </div>
    );
};

export default GraphCanvas;