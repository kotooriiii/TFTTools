import React from 'react';
import { UnitData } from '../../types/compBuilderTypes';
import { getHexCenter, getHexPoints, getCostColor, hexId, HEX_SIZE } from './hexUtils';
import { UnitPortrait } from '../UnitPortrait.tsx';

interface HexTileProps {
    row: number;
    col: number;
    unit: UnitData | null;
    isDragOver: boolean;
    readOnly?: boolean;
    onDrop?: (e: React.DragEvent, id: string) => void;
    onDragOver?: (e: React.DragEvent, id: string) => void;
    onDragLeave?: () => void;
    onDragStart?: (e: React.DragEvent, id: string, unit: UnitData) => void;
    onRemove?: (id: string) => void;
}

export const HexTile: React.FC<HexTileProps> = ({
                                                      row,
                                                      col,
                                                      unit,
                                                      isDragOver,
                                                      readOnly = false,
                                                      onDrop,
                                                      onDragOver,
                                                      onDragLeave,
                                                      onDragStart,
                                                      onRemove
                                                  }) => {
    const id = hexId(row, col);
    const { x, y } = getHexCenter(row, col);
    const points = getHexPoints(x, y);
    const fillColor = unit ? getCostColor(unit.cost) : (isDragOver ? '#d9c9a3' : '#efe8da');

    return (
        <g>
            <polygon
                points={points}
                fill={fillColor}
                fillOpacity={unit ? 0.25 : 1}
                stroke={isDragOver ? '#f1c40f' : '#8a7860'}
                strokeWidth={isDragOver ? 3 : 1.5}
                onDrop={readOnly ? undefined : (e) => onDrop?.(e, id)}
                onDragOver={readOnly ? undefined : (e) => onDragOver?.(e, id)}
                onDragLeave={readOnly ? undefined : onDragLeave}
                style={{ transition: 'fill 0.15s ease, stroke 0.15s ease' }}
            />
            {unit && (
                <foreignObject
                    x={x - (Math.sqrt(3) * HEX_SIZE) / 2}
                    y={y - HEX_SIZE}
                    width={Math.sqrt(3) * HEX_SIZE}
                    height={HEX_SIZE * 2}
                >
                    <div
                        draggable={!readOnly}
                        onDragStart={readOnly ? undefined : (e) => onDragStart?.(e, id, unit)}
                        onClick={readOnly ? undefined : () => onRemove?.(id)}
                        title={readOnly ? unit.displayName : `${unit.displayName} — click to remove`}
                        style={{
                            width: '100%',
                            height: '100%',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            flexDirection: 'column',
                            gap: 2,
                            cursor: readOnly ? 'default' : 'grab',
                            userSelect: 'none'
                        }}
                    >
                        <UnitPortrait
                            displayName={unit.displayName}
                            iconUrl={unit.iconUrl}
                            size={HEX_SIZE * 0.85}
                            borderColor={getCostColor(unit.cost)}
                            style={{ boxShadow: '0 2px 4px rgba(0,0,0,0.25)' }}
                        />
                        <div
                            style={{
                                fontSize: HEX_SIZE * 0.19,
                                lineHeight: 1.1,
                                fontWeight: 600,
                                color: 'var(--color-text-primary)',
                                textAlign: 'center',
                                maxWidth: '100%',
                                overflow: 'hidden',
                                textOverflow: 'ellipsis',
                                whiteSpace: 'nowrap'
                            }}
                        >
                            {unit.displayName}
                        </div>
                    </div>
                </foreignObject>
            )}
        </g>
    );
};
