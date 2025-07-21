import React from 'react';
import {HomePage} from '../pages/HomePage';
import GraphCanvasTool from '../tools/GraphCanvasTool';
import PlaceholderTool from '../tools/PlaceholderTool';

export interface RouteConfig
{
    id: string;
    path: string;
    name: string;
    icon: string;
    description?: string;
    component: React.ComponentType;
}

export const ROUTE_CONFIG: RouteConfig[] = [
    {
        id: 'home',
        path: '/',
        name: 'Home',
        icon: '🏠',
        description: 'Dashboard overview and welcome',
        component: HomePage
    },
    {
        id: 'graph-canvas',
        path: '/tools/graph-canvas',
        name: 'Graph Canvas',
        icon: '🔗',
        description: 'Interactive graph visualization and editing',
        component: GraphCanvasTool
    },
    {
        id: 'horizontal-comp-generator',
        path: '/tools/horizontal-comp-generator',
        name: 'Horizontal Comp Generator',
        icon: '🧬',
        description: 'Generate horizontal compositions based in filter criteria',
        component: PlaceholderTool
    }
];

// Helper to get just the tool info (for sidebar)
export const getToolsForSidebar = () =>
    ROUTE_CONFIG.map(({component: _component, ...tool}) => tool);