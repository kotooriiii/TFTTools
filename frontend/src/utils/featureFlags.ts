const EXPERIMENTAL_THEMES_KEY = 'tfttools:experimentalThemes';

export const isExperimentalThemesEnabled = (): boolean =>
    localStorage.getItem(EXPERIMENTAL_THEMES_KEY) === 'true';

declare global {
    interface Window {
        tfttools: {
            enableExperimentalThemes: () => void;
            disableExperimentalThemes: () => void;
        };
    }
}

export const registerThemeFeatureFlagConsoleHelpers = (): void => {
    window.tfttools = {
        ...window.tfttools,
        enableExperimentalThemes: () => {
            localStorage.setItem(EXPERIMENTAL_THEMES_KEY, 'true');
            console.log('Experimental themes enabled. Reloading...');
            location.reload();
        },
        disableExperimentalThemes: () => {
            localStorage.removeItem(EXPERIMENTAL_THEMES_KEY);
            console.log('Experimental themes disabled. Reloading...');
            location.reload();
        }
    };
};
