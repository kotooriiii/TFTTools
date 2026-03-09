import { EmblemItem, TraitItem, SearchItem, ApiEmblemResponse, ApiTraitResponse, ApiChampionResponse} from '../types/searchTypes';

interface MultiSearchConfig<TResponse, TResult> {
    endpoint: string;
    responseMappers: readonly {
        responseKey: string;
        transform: (item: TResponse) => TResult;
    }[];
}

// Single factory function for all search methods
const createSearchMethod = <TResponse, TResult>(config: MultiSearchConfig<TResponse, TResult>) =>
    async (query: string): Promise<TResult[]> => {
        if (!query.trim()) return [];

        try {
            const response = await fetch(`http://localhost:8080${config.endpoint}?query=${encodeURIComponent(query)}`);
            if (!response.ok) throw new Error(`Search request failed: ${response.status}`);
            
            const data = await response.json();
            const results: TResult[] = [];
            
            config.responseMappers.forEach(mapper => {
                const items = data[mapper.responseKey] || [];
                results.push(...items.map(mapper.transform));
            });
            
            return results;
        } catch (error) {
            console.error('Error during search:', error);
            return [];
        }
    };

// All configurations
const searchConfigs = {
    champions: {
        endpoint: '/search/champions',
        responseMappers: [{
            responseKey: 'championList',
            transform: (item: ApiChampionResponse): SearchItem => ({
                displayName: item.displayName,
            })
        }]
    } satisfies MultiSearchConfig<ApiChampionResponse, SearchItem>,
    emblems: {
        endpoint: '/search/emblems',
        responseMappers: [{
            responseKey: 'emblemList',
            transform: (item: ApiEmblemResponse): EmblemItem => ({
                displayName: item.displayName,
                count: 1
            })
        }]
    } satisfies MultiSearchConfig<ApiEmblemResponse, EmblemItem>,
    
    traits: {
        endpoint: '/search/traits',
        responseMappers: [{
            responseKey: 'traitList',
            transform: (item: ApiTraitResponse): TraitItem => ({
                displayName: item.displayName,
                count: item.activationThresholds?.[0] || 1,
                activationThresholds: item.activationThresholds || []
            })
        }]
    } satisfies MultiSearchConfig<ApiTraitResponse, TraitItem>,
    
    units: {
        endpoint: '/units/search',
        responseMappers: [
            {
                responseKey: 'championList',
                transform: (item: ApiChampionResponse): SearchItem => ({
                    displayName: item.displayName,
                })
            },
            {
                responseKey: 'traitList',
                transform: (item: ApiTraitResponse): SearchItem => ({
                    displayName: item.displayName,
                })
            }
        ]
    } satisfies MultiSearchConfig<ApiChampionResponse | ApiTraitResponse, SearchItem>
} as const

// All methods created the same way - completely consistent!
export const searchService = {
    searchChampions: createSearchMethod(searchConfigs.champions),
    searchEmblems: createSearchMethod(searchConfigs.emblems),
    searchTraits: createSearchMethod(searchConfigs.traits),
    searchUnits: createSearchMethod(searchConfigs.units)
};