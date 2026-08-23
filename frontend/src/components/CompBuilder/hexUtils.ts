export const HEX_COLS = 7;
export const HEX_ROWS = 4;
export const HEX_SIZE = 46; // center-to-corner radius

const HEX_WIDTH = Math.sqrt(3) * HEX_SIZE;
const HEX_HEIGHT = 2 * HEX_SIZE;
const ROW_SPACING = HEX_HEIGHT * 0.75;
const MARGIN = HEX_SIZE;

export interface HexCoord {
    col: number;
    row: number;
}

export const hexId = (col: number, row: number): string => `${col}-${row}`;

export const getHexCenter = (col: number, row: number): { x: number; y: number } => {
    const rowOffset = row % 2 === 1 ? HEX_WIDTH / 2 : 0;
    const x = MARGIN + HEX_WIDTH / 2 + col * HEX_WIDTH + rowOffset;
    const y = MARGIN + HEX_HEIGHT / 2 + row * ROW_SPACING;
    return { x, y };
};

export const getHexPoints = (cx: number, cy: number, size: number = HEX_SIZE): string => {
    const points: string[] = [];
    for (let i = 0; i < 6; i++) {
        const angleDeg = 60 * i - 30;
        const angleRad = (Math.PI / 180) * angleDeg;
        points.push(`${(cx + size * Math.cos(angleRad)).toFixed(2)},${(cy + size * Math.sin(angleRad)).toFixed(2)}`);
    }
    return points.join(' ');
};

export const BOARD_WIDTH = MARGIN * 2 + HEX_WIDTH * HEX_COLS + HEX_WIDTH / 2;
export const BOARD_HEIGHT = MARGIN * 2 + ROW_SPACING * (HEX_ROWS - 1) + HEX_HEIGHT;

export const generateHexCoords = (): HexCoord[] => {
    const coords: HexCoord[] = [];
    for (let row = 0; row < HEX_ROWS; row++) {
        for (let col = 0; col < HEX_COLS; col++) {
            coords.push({ col, row });
        }
    }
    return coords;
};

const COST_COLORS: Record<number, string> = {
    1: '#9AA5B1',
    2: '#3FA34D',
    3: '#3B82C4',
    4: '#A855C7',
    5: '#E8B923',
};

export const getCostColor = (cost: number): string => COST_COLORS[cost] ?? '#6B7280';

export const getInitials = (name: string): string => {
    const parts = name.trim().split(/\s+/);
    if (parts.length === 1) return parts[0].slice(0, 2).toUpperCase();
    return (parts[0][0] + parts[1][0]).toUpperCase();
};
