import React, { createContext, useContext, useState, ReactNode, useEffect } from 'react';
import { defaultTheme } from '../themes/themeConfigurations';

export interface ColorPalette {

  bg: {
    primary: string;
    secondary: string;
    accent: string;
  }
  text: {
    primary: string;
    secondary: string;
    accent: string;
  }
  border: string;
  success: string;
  warning: string;
  error: string;
}

export interface Theme {
  name: string;
  colors: ColorPalette;
}

interface ThemeContextType {
  theme: Theme;
  setTheme: (theme: Theme) => void;
}

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export const ThemeProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [theme, setThemeState] = useState<Theme>(defaultTheme);

  // Apply theme variables to CSS root
  const applyThemeToRoot = (theme: Theme) => {
    const root = document.documentElement;
    
    // Handle nested color objects
    Object.entries(theme.colors).forEach(([key, value]) => {
      if (typeof value === 'object' && value !== null) {
        // Handle nested objects like primary: { bg: '...', text: '...' }
        Object.entries(value).forEach(([subKey, subValue]) => {
          root.style.setProperty(`--color-${key}-${subKey}`, subValue);
        });
      } else {
        // Handle simple string values
        root.style.setProperty(`--color-${key}`, value);
      }
    });
  };

  const setTheme = (newTheme: Theme) => {
    console.log('Setting theme:', newTheme.name);
    setThemeState(newTheme);
    applyThemeToRoot(newTheme);
  };

  // Apply initial theme on mount
  useEffect(() => {
    console.log('Applying initial theme');
    applyThemeToRoot(theme);
  }, []);

  return (
    <ThemeContext.Provider value={{ theme, setTheme }}>
      {children}
    </ThemeContext.Provider>
  );
};

export const useTheme = () => {
  const context = useContext(ThemeContext);
  if (context === undefined) {
    throw new Error('useTheme must be used within a ThemeProvider');
  }
  return context;
};