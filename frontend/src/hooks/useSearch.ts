import { useState, useEffect, useRef, RefObject } from 'react';
import { SearchResult, SelectedItem } from '../types/searchTypes';
import {searchService} from "../services/searchService.ts";

export const useSearch = (searchPanelRef: RefObject<HTMLDivElement>) => {
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState<SearchResult[]>([]);
    const [selectedItems, setSelectedItems] = useState<SelectedItem[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const shouldHandleClickOutside = useRef(false);

    useEffect(() => {
        shouldHandleClickOutside.current = searchResults.length > 0;
    }, [searchResults.length]);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (shouldHandleClickOutside.current &&
                searchPanelRef.current &&
                !searchPanelRef.current.contains(event.target as Node)) {
                setSearchResults([]);
                setSearchQuery('');
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, [searchPanelRef]);

    const handleSearchChange = async (query: string) => {
        setSearchQuery(query);

        if (query.trim() === '') {
            setSearchResults([]);
            return;
        }

        setIsLoading(true);
        try {
            const results = await searchService.searchUnits(query);
            setSearchResults(results);
        } catch (error) {
            console.error('Search error:', error);
            setSearchResults([]);
        } finally {
            setIsLoading(false);
        }
    };


    const addSelectedItem = (item: SearchResult) => {
        if (selectedItems.some(selected => selected.name === item.name)) {
            return;
        }
        setSelectedItems(prev => [...prev, item]);
        setSearchQuery('');
        setSearchResults([]);
    };

    const removeSelectedItem = (itemId: string) => {
        setSelectedItems(prev => prev.filter(item => item.name !== itemId));
    };

    return {
        searchQuery,
        searchResults,
        selectedItems,
        isLoading,
        handleSearchChange,
        addSelectedItem,
        removeSelectedItem
    };
};