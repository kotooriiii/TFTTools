import React from 'react';
import { useTheme } from '../contexts/ThemeContext';
import { themes } from '../themes/themeConfigurations';
import { isExperimentalThemesEnabled } from '../utils/featureFlags';
import { Button } from '../components/Button';

export const SettingsPage: React.FC = () => {
    const { theme, setTheme } = useTheme();
    const experimentalThemesEnabled = isExperimentalThemesEnabled();
    const availableThemes = Object.values(themes).filter(
        (candidate) => !candidate.experimental || experimentalThemesEnabled
    );

    return (
        <div className="p-8 max-w-2xl mx-auto min-h-screen">
            <h1 className="text-3xl font-bold text-primary mb-8">Settings</h1>

            <div className="bg-primary border border-border rounded-lg shadow-md p-6">
                <h2 className="text-lg font-semibold text-primary mb-4">Theme</h2>
                <div className="flex gap-3">
                    {availableThemes.map((candidate) => (
                        <Button
                            key={candidate.name}
                            variant="outline"
                            selected={theme.name === candidate.name}
                            onClick={() => setTheme(candidate)}
                            className={`px-4 py-2 rounded-lg capitalize ${theme.name === candidate.name ? '' : 'text-secondary'}`}
                        >
                            {candidate.name}
                        </Button>
                    ))}
                </div>
            </div>
        </div>
    );
};
