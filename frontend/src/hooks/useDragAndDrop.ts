import {RefObject, useState} from 'react';
import {Unit} from '../types/unitTypes';

export const useDragAndDrop = (
    _svgRef: RefObject<SVGSVGElement>,
    _panOffset: { x: number; y: number },
    _zoom: number
) =>
{
    const [draggedUnit, setDraggedUnit] = useState<Unit | null>(null);

    const handleUnitDragStart = (e: React.DragEvent, unit: Unit) =>
    {
        setDraggedUnit(unit);

        // Simple static drag image
        const dragImage = document.createElement('div');
        dragImage.textContent = '⚔️';
        dragImage.style.cssText = `
        position: absolute;
        top: -1000px;
        width: 60px;
        height: 60px;
        background: #8B7355;
        color: white;
        font-size: 24px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%;
        border: 2px solid #594b42;
    `;

        document.body.appendChild(dragImage);
        e.dataTransfer.setDragImage(dragImage, 30, 30);


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

    return {
        draggedUnit,
        setDraggedUnit,
        handleUnitDragStart,
        handleCanvasDragOver
    };
};