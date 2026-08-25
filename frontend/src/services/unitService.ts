import { UnitData } from '../types/compBuilderTypes';

interface ApiTraitResponse {
    displayName: string;
    activationThresholds?: number[];
}

interface ApiUnitDetailedResponse {
    displayName: string;
    traits?: ApiTraitResponse[];
    cost: number;
    iconUrl?: string;
}

export const unitService = {
    async getAllUnits(): Promise<UnitData[]> {
        try {
            const response = await fetch('http://localhost:8080/units?simple=false');

            if (!response.ok) {
                throw new Error(`Failed to load units: ${response.status}`);
            }

            const data: ApiUnitDetailedResponse[] = await response.json();

            return data.map(unit => ({
                displayName: unit.displayName,
                cost: unit.cost,
                iconUrl: unit.iconUrl,
                traits: (unit.traits ?? []).map(trait => ({
                    displayName: trait.displayName,
                    activationThresholds: trait.activationThresholds ?? []
                }))
            }));
        } catch (error) {
            console.error('Error loading units:', error);
            return [];
        }
    }
};
