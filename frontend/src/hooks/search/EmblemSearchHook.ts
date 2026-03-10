import { RefObject } from 'react';
import { EmblemItem } from '../../types/searchTypes';
import { searchService } from '../../services/searchService';
import { useGenericSearch } from './GenericSearchHook.ts';

export const useEmblemSearch = (searchPanelRef: RefObject<HTMLDivElement>) => {
    const genericSearch = useGenericSearch<EmblemItem>({
        searchFunction: searchService.searchEmblems,
        searchPanelRef
    });

    const addSelectedItem = (item: EmblemItem) => {
        genericSearch.addSelectedItem(item, (item: EmblemItem) => item.displayName);
    };

    const removeSelectedItem = (itemId: string) => {
        genericSearch.removeSelectedItem(itemId, (item: EmblemItem) => item.displayName);
    };

    return {
        ...genericSearch,
        addSelectedItem,
        removeSelectedItem
    };
};
