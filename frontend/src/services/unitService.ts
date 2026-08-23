import { ChampionData } from '../types/compBuilderTypes';

interface ApiTraitResponse {
    displayName: string;
    activationThresholds?: number[];
}

interface ApiUnitDetailedResponse {
    displayName: string;
    traits?: ApiTraitResponse[];
    cost: number;
}

export const unitService = {
    async getAllChampions(): Promise<ChampionData[]> {
        try {
            const response = await fetch('http://localhost:8080/units?simple=false');

            if (!response.ok) {
                throw new Error(`Failed to load champions: ${response.status}`);
            }

            const data: ApiUnitDetailedResponse[] = await response.json();

            return data.map(unit => ({
                displayName: unit.displayName,
                cost: unit.cost,
                traits: (unit.traits ?? []).map(trait => ({
                    displayName: trait.displayName,
                    activationThresholds: trait.activationThresholds ?? []
                }))
            }));
        } catch (error) {
            console.error('Error loading champions:', error);
            return [];
        }
    }
};
