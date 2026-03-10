import { useImperativeHandle, forwardRef } from 'react';
import { GenericSearchBox } from './GenericSearchBox';
import { EmblemItem } from '../../types/searchTypes';
import { searchService } from '../../services/searchService';
import {useItemSearch} from "../../hooks/Horizontal/useItemSearchHook.ts";

export interface EmblemSearchBoxHandle {
    getSelectedEmblems: () => EmblemItem[];
    clearSelection: () => void;
}

export const EmblemSearchBox = forwardRef<EmblemSearchBoxHandle>(
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
        } = useItemSearch<EmblemItem>({
            searchFunction: searchService.searchEmblems,
            getItemKey: (item) => item.displayName,
        });

        useImperativeHandle(ref, () => ({
            getSelectedEmblems: getSelectedItems,
            clearSelection,
        }));

        const emblemConfig = {
            placeholder: "Search for trait emblems...",
            label: "Available Emblems",
            icon: "🏅",
            badgeColor: "bg-amber-100 text-amber-800",
            badgeText: "Emblem",
            displayName: (item: EmblemItem) => item.displayName,
            itemKey: (item: EmblemItem) => item.displayName
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
                config={emblemConfig}
                showCount={true}
                getItemCount={(emblem: EmblemItem) => emblem.count}
                isLoading={isLoading}
            />
        );
    }
);

EmblemSearchBox.displayName = 'EmblemSearchBox';