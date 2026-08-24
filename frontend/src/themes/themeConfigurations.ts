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
                            secondary: '#DDEAE3', // Misty sky
                            accent: '#F0DEC2'      // Pale straw dirt
                        },
                    text:
                        {
                            primary: '#3A4A38',
                            secondary: '#7E8A70',
                            accent: '#3A4A38'
                        },
                    border: '#A8C4AE',        // Soft misty sage for borders
                    success: '#7BC47F',
                    warning: '#E0A85C',
                    error: '#E17B72'
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

        },
        exp1: {
            name: 'exp1',
            experimental: true,
            colors:
                {
                    bg:
                        {
                            primary: '#EFE7F7',
                            secondary: '#F7E7F0',
                            accent: '#E7EEF7'
                        },
                    text:
                        {
                            primary: '#4A3F5C',
                            secondary: '#8A7A9E',
                            accent: '#4A3F5C'
                        },
                    border: '#D8C7E8',
                    success: '#7BC47F',
                    warning: '#E0A85C',
                    error: '#E17B72'
                }
        },
        exp2: {
            name: 'exp2',
            experimental: true,
            colors:
                {
                    bg:
                        {
                            primary: '#FDEDE3',
                            secondary: '#FDE3EA',
                            accent: '#FFF1D6'
                        },
                    text:
                        {
                            primary: '#6B4A3E',
                            secondary: '#A9806E',
                            accent: '#6B4A3E'
                        },
                    border: '#F3C9B6',
                    success: '#7BC47F',
                    warning: '#E0A85C',
                    error: '#E17B72'
                }
        },
        exp3: {
            name: 'exp3',
            experimental: true,
            colors:
                {
                    bg:
                        {
                            primary: '#E3F3F7',
                            secondary: '#E3F7EC',
                            accent: '#EAF0FA'
                        },
                    text:
                        {
                            primary: '#2E4A52',
                            secondary: '#6E9199',
                            accent: '#2E4A52'
                        },
                    border: '#BEE0E8',
                    success: '#7BC47F',
                    warning: '#E0A85C',
                    error: '#E17B72'
                }
        }
    }
;

export const defaultTheme = themes.default;