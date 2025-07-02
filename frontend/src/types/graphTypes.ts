import { Unit } from './unitTypes';

export const CIRCLE_RADIUS = 20;

export type Vertex = {
    id: number;
    x: number;
    y: number;
    unitData?: Unit;
};

export type Edge = {
    id: number;
    sourceId: number;
    targetId: number;
    label?: string;
};