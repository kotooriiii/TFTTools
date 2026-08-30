export interface TraitData {
    displayName: string;
    activationThresholds: number[];
    count: number;
}

export interface UnitData
{
    displayName: string;
    cost: number;
    traits: TraitData[];
    iconUrl?: string;
}
