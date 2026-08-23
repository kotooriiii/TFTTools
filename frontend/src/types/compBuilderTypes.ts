export interface TraitData {
    displayName: string;
    activationThresholds: number[];
}

export interface ChampionData {
    displayName: string;
    cost: number;
    traits: TraitData[];
}
