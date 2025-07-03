export type Champion = {displayName: string}
export type Trait = {displayName: string}

export type SearchResult =
    {
        championList: Champion[],
        traitList: Trait[],
    };

export type SearchItem =
    {
        name: string;
        type: 'champion' | 'trait';
    };

export type SelectedItem = {
    name: string;
    type: 'champion' | 'trait';
};