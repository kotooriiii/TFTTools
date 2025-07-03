import {useState, RefObject, useEffect} from 'react';
import { Vertex } from '../types/graphTypes';

const MIN_ZOOM = 0.6;
const MAX_ZOOM = 2;

export const useGraphInteractions = (svgRef: RefObject<SVGSVGElement>) => {
    const [isPanning, setIsPanning] = useState(false);
    const [panOffset, setPanOffset] = useState({ x: 0, y: 0 });
    const [lastPanPosition, setLastPanPosition] = useState<{ x: number; y: number } | null>(null);
    const [zoom, setZoom] = useState(1);
    const [draggingId, setDraggingId] = useState<number | null>(null);
    const [hoveringId, setHoveringId] = useState<number | null>(null);
    const [dragOffset, setDragOffset] = useState<{ x: number, y: number } | null>(null);
    const [panVelocity, setPanVelocity] = useState({ x: 0, y: 0 });

    const [mousePosition, setMousePosition] = useState<{ x: number; y: number } | null>(null);

    // Global mouse position tracker - this is the clean solution!
    useEffect(() => {
        const updateMousePosition = (e: MouseEvent) => {
            if (!svgRef.current) return;

            const motionG = svgRef.current.querySelector('g') as SVGGElement;
            if (motionG) {
                const ctm = motionG.getScreenCTM();
                if (ctm) {
                    const point = svgRef.current.createSVGPoint();
                    point.x = e.clientX;
                    point.y = e.clientY;
                    const svgPoint = point.matrixTransform(ctm.inverse());
                    setMousePosition({ x: svgPoint.x, y: svgPoint.y });
                }
            }
        };

        // Listen to global mouse movements - this captures ALL mouse events
        document.addEventListener('mousemove', updateMousePosition);
        document.addEventListener('pointermove', updateMousePosition);

        return () => {
            document.removeEventListener('mousemove', updateMousePosition);
            document.removeEventListener('pointermove', updateMousePosition);
        };
    }, []);


    const handleMouseDown = (e: React.MouseEvent) => {
        if (e.button !== 0 || e.target !== e.currentTarget) return;
        setIsPanning(true);
        setPanVelocity({ x: 0, y: 0 });
        setLastPanPosition({
            x: e.clientX,
            y: e.clientY
        });
    };

    const handleMouseMove = (e: React.MouseEvent) => {
        if (!isPanning || !svgRef.current) return;

        const mouseX = e.clientX ;
        const mouseY = e.clientY ;

        // Calculate delta with higher precision
        const deltaX = (mouseX - lastPanPosition.x);
        const deltaY = (mouseY - lastPanPosition.y);

        // Update pan offset
        setPanOffset(prev => ({
            x: prev.x + deltaX,
            y: prev.y + deltaY
        }));

        setPanVelocity({
            x: deltaX,
            y: deltaY
        });

        // Update last position and timestamp
        setLastPanPosition({
            x: mouseX,
            y: mouseY
        });
    };

    const handleMouseUp = () => {
        setIsPanning(false);
        setLastPanPosition(null);
    };


    const startDrag = (e: React.PointerEvent, id: number, vertices: Vertex[]) => {
        e.stopPropagation();
        e.preventDefault();
        e.currentTarget.setPointerCapture(e.pointerId);

        if (!svgRef.current) return;

        const rect = svgRef.current.getBoundingClientRect();
        const mouseX = (e.clientX - rect.left - panOffset.x) / zoom;
        const mouseY = (e.clientY - rect.top - panOffset.y) / zoom;

        const vertex = vertices.find(v => v.id === id);
        if (vertex) {
            setDragOffset({
                x: mouseX - vertex.x,
                y: mouseY - vertex.y
            });
        }
        setDraggingId(id);
    };

    const dragMove = (e: React.PointerEvent, setVertices: React.Dispatch<React.SetStateAction<Vertex[]>>) => {
        if (!draggingId || !dragOffset || !svgRef.current) return;

        const rect = svgRef.current.getBoundingClientRect();

        const mouseX = (e.clientX - rect.left - panOffset.x) / zoom;
        const mouseY = (e.clientY - rect.top - panOffset.y) / zoom;

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

    const endDrag = () => {
        setDraggingId(null);
        setDragOffset(null);
    };

    // Function to pan to a specific unit on the canvas
    const panToUnit = (unitX: number, unitY: number) => {
        if (!svgRef.current) return;

        const rect = svgRef.current.getBoundingClientRect();
        const centerX = rect.width / 2;
        const centerY = rect.height / 2;

        // Calculate the new pan offset to center the unit
        const newPanX = centerX - unitX * zoom;
        const newPanY = centerY - unitY * zoom;





        setPanOffset({ x: newPanX, y: newPanY });
        setPanVelocity({ x: 0, y: 0 }); // Reset velocity for smooth animation
    };

    // FIXED: Zoom button handlers now maintain proper coordinate centering
    const handleZoomIn = () => {
        const newZoom = Math.min(MAX_ZOOM, zoom * 1.2);
        if (newZoom === zoom) return;

        // Center zoom on the middle of the viewport
        const rect = svgRef.current.getBoundingClientRect();
        const centerX = rect.width / 2;
        const centerY = rect.height / 2;

        // Calculate the point to zoom into (center of viewport relative to current transform)
        const zoomPointX = (centerX - panOffset.x ) / zoom;
        const zoomPointY = (centerY - panOffset.y ) / zoom;

        // Update pan offset to keep zoom centered
        const newPanX = centerX - zoomPointX * newZoom;
        const newPanY = centerY - zoomPointY * newZoom;

        setZoom(newZoom);
        setPanOffset({ x: newPanX, y: newPanY });
    };



    const handleZoomOut = () => {
        const newZoom = Math.max(MIN_ZOOM, zoom / 1.2);

        // Center zoom on the middle of the viewport
        const rect = svgRef.current.getBoundingClientRect();
        const centerX = rect.width / 2;
        const centerY = rect.height / 2;

        // Calculate the point to zoom into (center of viewport relative to current transform)
        const zoomPointX = (centerX - panOffset.x ) / zoom;
        const zoomPointY = (centerY - panOffset.y ) / zoom;

        // Update pan offset to keep zoom centered
        const newPanX = centerX - zoomPointX * newZoom;
        const newPanY = centerY - zoomPointY * newZoom;

        setZoom(newZoom);
        setPanOffset({ x: newPanX, y: newPanY });

    };

    const handleWheel = (e: React.WheelEvent) => {
        if (!svgRef.current) return;

        const rect = svgRef.current.getBoundingClientRect();
        const mouseX = e.clientX - rect.left;
        const mouseY = e.clientY - rect.top;

        const isZoomOut = e.deltaY > 0;
        let newZoom;
        if (isZoomOut) {
            newZoom = Math.max(MIN_ZOOM, zoom / 1.2);
        } else {
            newZoom = Math.min(MAX_ZOOM, zoom * 1.2);
        }

        if (newZoom !== zoom) {
            const zoomPointX = (mouseX - panOffset.x) / zoom;
            const zoomPointY = (mouseY - panOffset.y) / zoom;
            const newPanX = mouseX - zoomPointX * newZoom;
            const newPanY = mouseY - zoomPointY * newZoom;

            setZoom(newZoom);
            setPanOffset({ x: newPanX, y: newPanY });
        }
    };


    const handleResetZoom = () => {
        setZoom(1);
        setPanOffset({ x: 0, y: 0 });
    };

    return {
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
        handleZoomIn,
        handleZoomOut,
        handleResetZoom,
        mousePosition,
        panToUnit,
    };
};