export interface TraitData {
    displayName: string;
    activationThresholds: number[];
}

export interface UnitData
{
    displayName: string;
    cost: number;
    traits: TraitData[];
    iconUrl?: string;
}
