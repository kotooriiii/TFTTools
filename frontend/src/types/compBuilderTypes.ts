export interface TraitData {
    apiName: string;
    displayName: string;
    activationThresholds: number[];
    count: number;
}

export interface UnitData
{
    apiName: string;
    displayName: string;
    cost: number;
    traits: TraitData[];
    iconUrl?: string;
}
