import { useState, useEffect, useRef, RefObject } from 'react';
import {SearchItem, SelectedItem} from '../types/searchTypes';
import {searchService} from "../services/searchService.ts";

export const useSearch = (searchPanelRef: RefObject<HTMLDivElement>) => {
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResultItems, setSearchResultItems] = useState<SearchItem[]>([]);
    const [selectedItems, setSelectedItems] = useState<SelectedItem[]>([]);
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
            const results: SearchItem[] = await searchService.searchUnits(query);
            setSearchResultItems(results);
        } catch (error) {
            console.error('Search error:', error);
            setSearchResultItems([]);
        } finally {
            setIsLoading(false);
        }
    };


    const addSelectedItem = (item: SearchItem) => {
        if (selectedItems.some(selected => selected.name === item.name)) {
            return;
        }
        setSelectedItems(prev => [...prev, item]);
        setSearchQuery('');
        setSearchResultItems([]);
    };

    const removeSelectedItem = (itemId: string) => {
        setSelectedItems(prev => prev.filter(item => item.name !== itemId));
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