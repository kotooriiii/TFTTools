import React from 'react';
import { ChampionData } from '../../types/compBuilderTypes';
import { BOARD_HEIGHT, BOARD_WIDTH, generateHexCoords, hexId } from './hexUtils';
import { HexTile } from './HexTile';

interface HexBoardProps {
    board: Record<string, ChampionData>;
    dragOverHexId?: string | null;
    readOnly?: boolean;
    onDrop?: (e: React.DragEvent, id: string) => void;
    onDragOver?: (e: React.DragEvent, id: string) => void;
    onDragLeave?: () => void;
    onHexDragStart?: (e: React.DragEvent, id: string, champion: ChampionData) => void;
    onRemove?: (id: string) => void;
}

const hexCoords = generateHexCoords();

export const HexBoard: React.FC<HexBoardProps> = ({
                                                        board,
                                                        dragOverHexId = null,
                                                        readOnly = false,
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
            {hexCoords.map(({ row, col }) => {
                const id = hexId(row, col);
                return (
                    <HexTile
                        key={id}
                        row={row}
                        col={col}
                        champion={board[id] ?? null}
                        isDragOver={dragOverHexId === id}
                        readOnly={readOnly}
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
