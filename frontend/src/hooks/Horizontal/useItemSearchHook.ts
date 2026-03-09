import { useState, useCallback } from 'react';
import {SearchItem} from "../../types/searchTypes.ts";

interface UseItemSearchConfig<T extends SearchItem> {
    searchFunction: (query: string) => Promise<T[]>;
    getItemKey: (item: T) => string;
    getItemAllowedCounts?: (item: T) => number[] | undefined; // Get allowed counts for an item
}

export const useItemSearch = <T extends SearchItem>({
                                                        searchFunction,
                                                        getItemKey,
                                                        getItemAllowedCounts
                                                    }: UseItemSearchConfig<T>) => {
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState<T[]>([]);
    const [selectedItems, setSelectedItems] = useState<T[]>([]);
    const [isLoading, setIsLoading] = useState(false);

    const handleSearchChange = useCallback(async (query: string) => {
        setSearchQuery(query);
        if (query.length > 0) {
            setIsLoading(true);
            try {
                const results = await searchFunction(query);
                setSearchResults(results);
            } catch (error) {
                console.error('Search error:', error);
                setSearchResults([]);
            } finally {
                setIsLoading(false);
            }
        } else {
            setSearchResults([]);
            setIsLoading(false);
        }
    }, [searchFunction]);

    const handleAddItem = useCallback((item: T) => {
        const itemKey = getItemKey(item);
        if (!selectedItems.find(selected => getItemKey(selected) === itemKey)) {
            let newItem = item;


            const defaultCount = 1;
            const allowedCounts = getItemAllowedCounts?.(item);

            // Use the first allowed count or the default count
            const initialCount = allowedCounts ? allowedCounts[0] : defaultCount;
            newItem = { ...item, count: initialCount } as T;

            setSelectedItems(prev => [...prev, newItem]);
        }
        setSearchQuery('');
        setSearchResults([]);
    }, [selectedItems, getItemKey, getItemAllowedCounts]);

    const handleRemoveItem = useCallback((key: string) => {
        setSelectedItems(prev => prev.filter(item => getItemKey(item) !== key));
    }, [getItemKey]);

    const handleUpdateCount = useCallback((key: string, count: number) => {
        setSelectedItems(prev =>
            prev.map(item => {
                if (getItemKey(item) === key) {
                    // Validate count if getAllowedCounts is provided
                    const allowedCounts = getItemAllowedCounts?.(item);
                    if (allowedCounts && !allowedCounts.includes(count)) {
                        console.warn(`Invalid count ${count} for ${getItemKey(item)}. Allowed values: ${allowedCounts.join(', ')}`);
                        return item; // Don't update if invalid
                    }
                    return { ...item, count } as T;
                }
                return item;
            })
        );
    }, [getItemKey, getItemAllowedCounts]);

    // Imperative handle methods
    const getSelectedItems = useCallback(() => selectedItems, [selectedItems]);
    const clearSelection = useCallback(() => setSelectedItems([]), []);

    return {
        // State
        searchQuery,
        searchResults,
        selectedItems,
        isLoading,

        // Handlers
        handleSearchChange,
        handleAddItem,
        handleRemoveItem,
        handleUpdateCount,

        // Imperative methods
        getSelectedItems,
        clearSelection,
        getItemAllowedCounts
    };
};