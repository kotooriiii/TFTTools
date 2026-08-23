import React from 'react';
import { ChampionData } from '../../types/compBuilderTypes';
import { getHexCenter, getHexPoints, getCostColor, getInitials, hexId, HEX_SIZE } from './hexUtils';

interface HexTileProps {
    col: number;
    row: number;
    champion: ChampionData | null;
    isDragOver: boolean;
    onDrop: (e: React.DragEvent, id: string) => void;
    onDragOver: (e: React.DragEvent, id: string) => void;
    onDragLeave: () => void;
    onDragStart: (e: React.DragEvent, id: string, champion: ChampionData) => void;
    onRemove: (id: string) => void;
}

export const HexTile: React.FC<HexTileProps> = ({
                                                      col,
                                                      row,
                                                      champion,
                                                      isDragOver,
                                                      onDrop,
                                                      onDragOver,
                                                      onDragLeave,
                                                      onDragStart,
                                                      onRemove
                                                  }) => {
    const id = hexId(col, row);
    const { x, y } = getHexCenter(col, row);
    const points = getHexPoints(x, y);
    const fillColor = champion ? getCostColor(champion.cost) : (isDragOver ? '#d9c9a3' : '#efe8da');

    return (
        <g>
            <polygon
                points={points}
                fill={fillColor}
                fillOpacity={champion ? 0.25 : 1}
                stroke={isDragOver ? '#f1c40f' : '#8a7860'}
                strokeWidth={isDragOver ? 3 : 1.5}
                onDrop={(e) => onDrop(e, id)}
                onDragOver={(e) => onDragOver(e, id)}
                onDragLeave={onDragLeave}
                style={{ transition: 'fill 0.15s ease, stroke 0.15s ease' }}
            />
            {champion && (
                <foreignObject
                    x={x - (Math.sqrt(3) * HEX_SIZE) / 2}
                    y={y - HEX_SIZE}
                    width={Math.sqrt(3) * HEX_SIZE}
                    height={HEX_SIZE * 2}
                >
                    <div
                        draggable
                        onDragStart={(e) => onDragStart(e, id, champion)}
                        onClick={() => onRemove(id)}
                        title={`${champion.displayName} — click to remove`}
                        style={{
                            width: '100%',
                            height: '100%',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            flexDirection: 'column',
                            gap: 2,
                            cursor: 'grab',
                            userSelect: 'none'
                        }}
                    >
                        <div
                            style={{
                                width: HEX_SIZE * 0.85,
                                height: HEX_SIZE * 0.85,
                                borderRadius: '50%',
                                backgroundColor: '#3B3B1A',
                                color: 'white',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                fontSize: HEX_SIZE * 0.3,
                                fontWeight: 700,
                                border: `2px solid ${getCostColor(champion.cost)}`,
                                boxShadow: '0 2px 4px rgba(0,0,0,0.25)'
                            }}
                        >
                            {getInitials(champion.displayName)}
                        </div>
                        <div
                            style={{
                                fontSize: HEX_SIZE * 0.19,
                                lineHeight: 1.1,
                                fontWeight: 600,
                                color: '#2C1D17',
                                textAlign: 'center',
                                maxWidth: '100%',
                                overflow: 'hidden',
                                textOverflow: 'ellipsis',
                                whiteSpace: 'nowrap'
                            }}
                        >
                            {champion.displayName}
                        </div>
                    </div>
                </foreignObject>
            )}
        </g>
    );
};
