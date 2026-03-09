import { Unit } from '../types/unitTypes';

export const filterService = {
    async filterUnits(champions: string[], traits: string[]): Promise<Unit[]> {
        const params = new URLSearchParams();

        if (champions.length > 0) {
            params.append('champions', champions.join(','));
        }

        if (traits.length > 0) {
            params.append('traits', traits.join(','));
        }

        try {
            const response = await fetch(`http://localhost:8080/units/filter?${params.toString()}`);

            if (!response.ok) {
                throw new Error('Filter request failed');
            }

            return await response.json();
        } catch (error) {
            console.error('Error during filtering:', error);
            return [];
        }
    }
};
