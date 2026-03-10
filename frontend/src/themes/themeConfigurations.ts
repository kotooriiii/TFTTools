// src/themes/themes.ts
import {Theme} from '../contexts/ThemeContext';

export const themes: Record<string, Theme> = {
        default: {
            name: 'default',
            colors:
                {
                    bg:
                        {
                            primary: '#E7EFC7',
                            secondary: '#c7e4ef',
                            accent: '#ceefc7'
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
                            primary: '#E7EFC7',
                            secondary: '#E7EFC7',
                            accent: '#E7EFC7'
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

        }
    }
;

export const defaultTheme = themes.default;