import { RefObject } from 'react';
import { SearchItem } from '../../types/searchTypes';
import { searchService } from '../../services/searchService';
import { useGenericSearch} from "./GenericSearchHook.ts";

export const useUnitSearch = (searchPanelRef: RefObject<HTMLDivElement>) => {
    const genericSearch = useGenericSearch<SearchItem>({
        searchFunction: searchService.searchUnits,
        searchPanelRef
    });

    const addSelectedItem = (item: SearchItem) => {
        genericSearch.addSelectedItem(item, (item: SearchItem) => item.displayName);
    };

    const removeSelectedItem = (itemId: string) => {
        genericSearch.removeSelectedItem(itemId, (item: SearchItem) => item.displayName);
    };

    return {
        ...genericSearch,
        addSelectedItem,
        removeSelectedItem
    };
};