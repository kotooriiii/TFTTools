import { SearchResult } from '../types/searchTypes';

export const searchService = {
    async searchUnits(query: string): Promise<SearchResult[]> {
        if (!query.trim()) return [];

        try {
            const response = await fetch(`http://localhost:8080/units/search?query=${encodeURIComponent(query)}`);

            if (!response.ok) {
                throw new Error('Search request failed');
            }

            return await response.json();
        } catch (error) {
            console.error('Error during search:', error);
            return [];
        }
    }
};
