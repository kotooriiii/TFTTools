import { useState, useEffect, useRef, RefObject } from 'react';

export interface SearchHookConfig<T> {
    searchFunction: (query: string) => Promise<T[]>;
    searchPanelRef: RefObject<HTMLDivElement>;
}

export const useGenericSearch = <T>({ searchFunction, searchPanelRef }: SearchHookConfig<T>) => {
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResultItems, setSearchResultItems] = useState<T[]>([]);
    const [selectedItems, setSelectedItems] = useState<T[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const shouldHandleClickOutside = useRef(false);

    useEffect(() => {
        shouldHandleClickOutside.current = searchResultItems.length > 0;
    }, [searchResultItems.length]);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (shouldHandleClickOutside.current &&
                searchPanelRef.current &&
                !searchPanelRef.current.contains(event.target as Node)) {
                setSearchResultItems([]);
                setSearchQuery('');
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, [searchPanelRef]);

    const handleSearchChange = async (query: string) => {
        setSearchQuery(query);

        if (query.trim() === '') {
            setSearchResultItems([]);
            return;
        }

        setIsLoading(true);
        try {
            const results = await searchFunction(query);
            setSearchResultItems(results);
        } catch (error) {
            console.error('Search error:', error);
            setSearchResultItems([]);
        } finally {
            setIsLoading(false);
        }
    };

    const addSelectedItem = (item: T, getItemKey: (item: T) => string) => {
        if (selectedItems.some(selected => getItemKey(selected) === getItemKey(item))) {
            return;
        }
        setSelectedItems(prev => [...prev, item]);
        setSearchQuery('');
        setSearchResultItems([]);
    };

    const removeSelectedItem = (itemKey: string, getItemKey: (item: T) => string) => {
        setSelectedItems(prev => prev.filter(item => getItemKey(item) !== itemKey));
    };

    return {
        searchQuery,
        searchResultItems,
        selectedItems,
        isLoading,
        handleSearchChange,
        addSelectedItem,
        removeSelectedItem
    };
};
