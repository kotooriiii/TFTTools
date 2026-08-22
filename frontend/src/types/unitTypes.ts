export type Unit = {
    displayName: string;
    traits: string[];
};

export type Trait = {
    displayName: string;
    activationThresholds?: number[];
}

export type Emblem = {
    displayName: string;
    trait: Trait;
}

export type Composition = Unit[];
