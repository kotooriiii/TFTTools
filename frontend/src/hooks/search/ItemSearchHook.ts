import { RefObject } from 'react';
import { SearchItem } from '../../types/searchTypes';
import { searchService } from '../../services/searchService';
import { useGenericSearch} from "./GenericSearchHook.ts";

export const useSearch = (searchPanelRef: RefObject<HTMLDivElement | null>) => {
    const genericSearch = useGenericSearch<SearchItem>({
        searchFunction: searchService.searchAny,
        searchPanelRef
    });

    const addSelectedItem = (item: SearchItem) => {
        genericSearch.addSelectedItem(item, (item: SearchItem) => item.apiName);
    };

    const removeSelectedItem = (itemId: string) => {
        genericSearch.removeSelectedItem(itemId, (item: SearchItem) => item.apiName);
    };

    return {
        ...genericSearch,
        addSelectedItem,
        removeSelectedItem
    };
};