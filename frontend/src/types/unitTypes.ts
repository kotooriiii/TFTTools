export type Trait = {
    apiName: string;
    displayName: string;
    activationThresholds?: number[];
}

export type Unit = {
    apiName: string;
    displayName: string;
    traits: Trait[];
    iconUrl?: string;
    cost?: number;
};

export type Emblem = {
    apiName: string;
    displayName: string;
    trait: Trait;
}

export type Composition = Unit[];
