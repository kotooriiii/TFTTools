// src/themes/themes.ts
import {Theme} from '../contexts/ThemeContext';

export const themes: Record<string, Theme> = {
        default: {
            name: 'default',
            colors:
                {
                    bg:
                        {
                            primary: '#E7EFC7',   // Grass
                            secondary: '#DCEAF0', // Sky
                            accent: '#EDE0C8'      // Dirt
                        },
                    text:
                        {
                            primary: '#3B3B1A',
                            secondary: '#8A784E',
                            accent: '#3B3B1A'
                        },
                    border: '#AEC8A4',        // Soft sage green for borders
                    success: '#4CAF50',
                    warning: '#FF9800',
                    error: '#F44336'
                }
        },
        dark: {
            name: 'dark',
            colors:
                {
                    bg:
                        {
                            primary: '#2B3324',   // Night grass
                            secondary: '#26313A', // Night sky
                            accent: '#3A2E22'      // Dark soil
                        },
                    text:
                        {
                            primary: '#EDE6D6',
                            secondary: '#B5AE8E',
                            accent: '#EDE6D6'
                        },
                    border: '#4B5240',        // Muted olive borders
                    success: '#7BC47F',
                    warning: '#E0A85C',
                    error: '#E17B72'
                }

        }
    }
;

export const defaultTheme = themes.default;