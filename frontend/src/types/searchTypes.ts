export interface SearchItem {
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
    displayName: string;
}

export interface ApiTraitResponse {
    displayName: string;
    activationThresholds?: number[];
}

export interface ApiUnitResponse
{
    displayName: string;
    iconUrl?: string;
}