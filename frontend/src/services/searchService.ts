import {SearchItem, SearchResult} from '../types/searchTypes';

export const searchService = {
    async searchUnits(query: string): Promise<SearchItem[]> {
        if (!query.trim()) return [];

        try {
            const response = await fetch(`http://localhost:8080/units/search?query=${encodeURIComponent(query)}`);

            if (!response.ok) {
                throw new Error('Search request failed');
            }

            const searchResult: SearchResult = await response.json();
            
            const searchItems: SearchItem[] = [];

                const championList = searchResult.championList;
                championList.forEach(champion => {searchItems.push({name: champion.displayName, type: 'champion'})});
                
                const traitList = searchResult.traitList;
                traitList.forEach(trait => {searchItems.push({name: trait.displayName, type: 'trait'})});


            return searchItems;
        } catch (error) {
            console.error('Error during search:', error);
            return [];
        }
    }
};
