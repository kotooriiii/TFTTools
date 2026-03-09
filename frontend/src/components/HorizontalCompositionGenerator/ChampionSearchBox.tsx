import { useImperativeHandle, forwardRef } from 'react';
import { GenericSearchBox } from './GenericSearchBox';
import {ChampionItem} from '../../types/searchTypes';
import { searchService } from '../../services/searchService';
import {useItemSearch} from "../../hooks/Horizontal/useItemSearchHook.ts";

export interface ChampionSearchBoxHandle {
    getSelectedChampions: () => ChampionItem[];
    clearSelection: () => void;
}

export const ChampionSearchBox = forwardRef<ChampionSearchBoxHandle>(
    (_, ref) => {
        const {
            searchQuery,
            searchResults,
            selectedItems,
            isLoading,
            handleSearchChange,
            handleAddItem,
            handleRemoveItem,
            handleUpdateCount,
            getSelectedItems,
            clearSelection,
        } = useItemSearch<ChampionItem>({
            searchFunction: searchService.searchChampions,
            getItemKey: (item) => item.displayName,
        });

        useImperativeHandle(ref, () => ({
            getSelectedChampions: getSelectedItems,
            clearSelection,
        }));

        const championConfig = {
            placeholder: "Search for champions...",
            label: "Available Champions",
            icon: "⚔️",
            badgeColor: "bg-amber-100 text-amber-800",
            badgeText: "Champion",
            displayName: (item: ChampionItem) => item.displayName,
            itemKey: (item: ChampionItem) => item.displayName
        };

        return (
            <GenericSearchBox
                searchQuery={searchQuery}
                searchResults={searchResults}
                selectedItems={selectedItems}
                onSearchChange={handleSearchChange}
                onAddItem={handleAddItem}
                onRemoveItem={handleRemoveItem}
                onUpdateCount={handleUpdateCount}
                config={championConfig}
                isLoading={isLoading}
            />
        );
    }
);

ChampionSearchBox.displayName = 'ChampionSearchBox';