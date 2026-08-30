export interface SearchItem {
    apiName: string;
    displayName: string;
    iconUrl?: string;
    type?: 'unit' | 'trait';
}

export type SelectedItem = SearchItem;

export type UnitItem = SearchItem;

export interface TraitItem extends SearchItem {
    count: number;
    activationThresholds?: number[]; // [3, 5, 7] for example
}

export interface EmblemItem extends SearchItem {
    count: number;
}

// API Response Types
export interface ApiEmblemResponse {
    apiName: string;
    displayName: string;
}

export interface ApiTraitResponse {
    apiName: string;
    displayName: string;
    activationThresholds?: number[];
}

export interface ApiUnitResponse
{
    apiName: string;
    displayName: string;
    iconUrl?: string;
}