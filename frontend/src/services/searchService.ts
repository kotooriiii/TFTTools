import { EmblemItem, TraitItem, SearchItem, ApiEmblemResponse, ApiTraitResponse, ApiChampionResponse} from '../types/searchTypes';

interface MultiSearchConfig<TResponse, TResult> {
    endpoint: string;
    responseMappers: readonly {
        responseKey?: string; // Make this optional since some endpoints return arrays directly
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
                // If responseKey is provided, use it; otherwise, use data directly (for endpoints that return arrays)
                const items = mapper.responseKey ? (data[mapper.responseKey] || []) : (Array.isArray(data) ? data : []);
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
        endpoint: '/units/search/champions',
        responseMappers: [{
            // Remove responseKey since the endpoint returns the array directly
            transform: (item: ApiChampionResponse): SearchItem => ({
                displayName: item.displayName,
            })
        }]
    } satisfies MultiSearchConfig<ApiChampionResponse, SearchItem>,
    
    emblems: {
        endpoint: '/units/search/emblems',
        responseMappers: [{
            // Remove responseKey since the endpoint returns the array directly
            transform: (item: ApiEmblemResponse): EmblemItem => ({
                displayName: item.displayName,
                count: 1
            })
        }]
    } satisfies MultiSearchConfig<ApiEmblemResponse, EmblemItem>,
    
    traits: {
        endpoint: '/units/search/traits',
        responseMappers: [{
            // Remove responseKey since the endpoint returns the array directly
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
                responseKey: 'championList', // Keep this one since /units/search returns a SearchResultDTO
                transform: (item: ApiChampionResponse): SearchItem => ({
                    displayName: item.displayName,
                })
            },
            {
                responseKey: 'traitList', // Keep this one since /units/search returns a SearchResultDTO
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