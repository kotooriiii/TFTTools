import { useImperativeHandle, forwardRef } from 'react';
import { GenericSearchBox } from './GenericSearchBox';
import { TraitItem } from '../../types/searchTypes';
import { searchService } from '../../services/searchService';
import {useItemSearch} from "../../hooks/Horizontal/useItemSearchHook.ts";

export interface TraitSearchBoxHandle {
    getSelectedTraits: () => TraitItem[];
    clearSelection: () => void;
}


export const TraitSearchBox = forwardRef<TraitSearchBoxHandle>(
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
            getItemAllowedCounts
            } = useItemSearch<TraitItem>({
            searchFunction: searchService.searchTraits,
            getItemKey: (item) => item.displayName,
            getItemAllowedCounts: (item) => item.activationThresholds
        });

        useImperativeHandle(ref, () => ({
            getSelectedTraits: getSelectedItems,
            clearSelection,
        }));

        const traitConfig = {
            placeholder: "Search for traits...",
            label: "Available Traits",
            icon: "⭐",
            badgeColor: "bg-purple-100 text-purple-800",
            badgeText: "Trait",
            displayName: (item: TraitItem) => item.displayName,
            itemKey: (item: TraitItem) => item.displayName
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
                config={traitConfig}
                showCount={true}
                getItemCount={(trait: TraitItem) => trait.count}
                getItemAllowedCounts={getItemAllowedCounts}
                isLoading={isLoading}
            />
        );
    }
);

TraitSearchBox.displayName = 'TraitSearchBox';