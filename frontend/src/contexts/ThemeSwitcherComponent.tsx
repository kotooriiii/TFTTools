// src/components/ThemeSwitcher.tsx
import { useTheme } from '../contexts/ThemeContext';
import { themes } from '../themes/themeConfigurations';
import {useEffect} from "react";

export const ThemeSwitcher: React.FC = () => {
  const { theme, setTheme } = useTheme();

  return (
    <div className="flex gap-2 p-4">
      {Object.entries(themes).map(([key, themeConfig]) => (
        <button
          key={key}
          onClick={() => setTheme(themeConfig)}
          className={`px-4 py-2 rounded transition-colors ${
            theme.name === themeConfig.name
              ? 'bg-primary text-white'
              : 'bg-surface border border-primary text-primary hover:bg-primary hover:text-white'
          }`}
        >
          {themeConfig.name.charAt(0).toUpperCase() + themeConfig.name.slice(1)}
        </button>
      ))}
    </div>
  );
};