import React from 'react';
import {HomePage} from '../pages/HomePage';
import TraitWebTool from '../tools/TraitWebTool';
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
    hideFromNav?: boolean;
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
        id: 'trait-web',
        path: '/tools/trait-web',
        name: 'Trait Web',
        icon: '🔗',
        description: 'Drag units onto a canvas and see them connect by shared traits',
        component: TraitWebTool
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
        description: 'Search units and traits and drag them onto a hex board to build compositions',
        component: CompBuilderTool
    },
    {
        id: 'login',
        path: '/login',
        name: 'Log In',
        icon: '🔑',
        component: LoginPage,
        hideFromNav: true
    },
    {
        id: 'signup',
        path: '/signup',
        name: 'Sign Up',
        icon: '📝',
        component: SignupPage,
        hideFromNav: true
    },
    {
        id: 'profile',
        path: '/profile',
        name: 'Profile',
        icon: '👤',
        component: ProfilePage,
        hideFromNav: true
    },
    {
        id: 'settings',
        path: '/settings',
        name: 'Settings',
        icon: '⚙️',
        component: SettingsPage,
        hideFromNav: true
    },
    {
        id: 'my-comps',
        path: '/my-comps',
        name: 'My Comps',
        icon: '📁',
        component: MyCompsPage,
        hideFromNav: true
    }
];

// Helper to get just the tool info (for nav)
export const getToolsForNav = () =>
    ROUTE_CONFIG
        .filter(tool => !tool.hideFromNav)
        .map(({component: _component, ...tool}) => tool);