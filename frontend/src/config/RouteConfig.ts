import React from 'react';
import {HomePage} from '../pages/HomePage';
import GraphCanvasTool from '../tools/GraphCanvasTool';
import HorizontalCompositionGenerator from "../tools/HorizontalCompositionGenerator.tsx";
import CompBuilderTool from "../tools/CompBuilderTool.tsx";
import {LoginPage} from '../pages/LoginPage';
import {SignupPage} from '../pages/SignupPage';
import {ProfilePage} from '../pages/ProfilePage';
import {SettingsPage} from '../pages/SettingsPage';
import {MyCompsPage} from '../pages/MyCompsPage';

export interface RouteConfig
{
    id: string;
    path: string;
    name: string;
    icon: string;
    description?: string;
    component: React.ComponentType;
    hideFromSidebar?: boolean;
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
        component: HorizontalCompositionGenerator
    },
    {
        id: 'comp-builder',
        path: '/tools/comp-builder',
        name: 'Comp Builder',
        icon: '⬡',
        description: 'Search champions and traits and drag them onto a hex board to build compositions',
        component: CompBuilderTool
    },
    {
        id: 'login',
        path: '/login',
        name: 'Log In',
        icon: '🔑',
        component: LoginPage,
        hideFromSidebar: true
    },
    {
        id: 'signup',
        path: '/signup',
        name: 'Sign Up',
        icon: '📝',
        component: SignupPage,
        hideFromSidebar: true
    },
    {
        id: 'profile',
        path: '/profile',
        name: 'Profile',
        icon: '👤',
        component: ProfilePage,
        hideFromSidebar: true
    },
    {
        id: 'settings',
        path: '/settings',
        name: 'Settings',
        icon: '⚙️',
        component: SettingsPage,
        hideFromSidebar: true
    },
    {
        id: 'my-comps',
        path: '/my-comps',
        name: 'My Comps',
        icon: '📁',
        component: MyCompsPage,
        hideFromSidebar: true
    }
];

// Helper to get just the tool info (for sidebar)
export const getToolsForSidebar = () =>
    ROUTE_CONFIG
        .filter(tool => !tool.hideFromSidebar)
        .map(({component: _component, ...tool}) => tool);