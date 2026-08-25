export type Trait = {
    displayName: string;
    activationThresholds?: number[];
}

export type Unit = {
    displayName: string;
    traits: Trait[];
    iconUrl?: string;
    cost?: number;
};

export type Emblem = {
    displayName: string;
    trait: Trait;
}

export type Composition = Unit[];
