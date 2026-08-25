// Shared visual conventions for rendering a unit, independent of any one tool's layout.

const COST_COLORS: Record<number, string> = {
    1: '#9AA5B1',
    2: '#3FA34D',
    3: '#3B82C4',
    4: '#A855C7',
    5: '#E8B923',
};

const DEFAULT_COST_COLOR = '#6B7280';

export const getCostColor = (cost?: number): string =>
    (cost !== undefined && COST_COLORS[cost]) || DEFAULT_COST_COLOR;

export const getInitials = (name: string): string => {
    const parts = name.trim().split(/\s+/);
    if (parts.length === 1) return parts[0].slice(0, 2).toUpperCase();
    return (parts[0][0] + parts[1][0]).toUpperCase();
};
