import React from 'react';
import { ChampionData } from '../../types/compBuilderTypes';
import { BOARD_HEIGHT, BOARD_WIDTH, generateHexCoords, hexId } from './hexUtils';
import { HexTile } from './HexTile';

interface HexBoardProps {
    board: Record<string, ChampionData>;
    dragOverHexId: string | null;
    onDrop: (e: React.DragEvent, id: string) => void;
    onDragOver: (e: React.DragEvent, id: string) => void;
    onDragLeave: () => void;
    onHexDragStart: (e: React.DragEvent, id: string, champion: ChampionData) => void;
    onRemove: (id: string) => void;
}

const hexCoords = generateHexCoords();

export const HexBoard: React.FC<HexBoardProps> = ({
                                                        board,
                                                        dragOverHexId,
                                                        onDrop,
                                                        onDragOver,
                                                        onDragLeave,
                                                        onHexDragStart,
                                                        onRemove
                                                    }) => {
    return (
        <svg
            viewBox={`0 0 ${BOARD_WIDTH} ${BOARD_HEIGHT}`}
            width="100%"
            style={{ maxWidth: 780 }}
        >
            {hexCoords.map(({ col, row }) => {
                const id = hexId(col, row);
                return (
                    <HexTile
                        key={id}
                        col={col}
                        row={row}
                        champion={board[id] ?? null}
                        isDragOver={dragOverHexId === id}
                        onDrop={onDrop}
                        onDragOver={onDragOver}
                        onDragLeave={onDragLeave}
                        onDragStart={onHexDragStart}
                        onRemove={onRemove}
                    />
                );
            })}
        </svg>
    );
};
