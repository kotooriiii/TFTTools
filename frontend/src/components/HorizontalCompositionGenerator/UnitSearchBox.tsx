import { useImperativeHandle, forwardRef } from 'react';
import { GenericSearchBox } from './GenericSearchBox';
import {UnitItem} from '../../types/searchTypes';
import { searchService } from '../../services/searchService';
import {useItemSearch} from "../../hooks/Horizontal/useItemSearchHook.ts";

export interface UnitSearchBoxHandle {
    getSelectedUnits: () => UnitItem[];
    clearSelection: () => void;
}

export const UnitSearchBox = forwardRef<UnitSearchBoxHandle>(
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
        } = useItemSearch<UnitItem>({
            searchFunction: searchService.searchUnits,
            getItemKey: (item) => item.displayName,
        });

        useImperativeHandle(ref, (): UnitSearchBoxHandle => ({
            getSelectedUnits: getSelectedItems,
            clearSelection,
        }));

        const unitConfig = {
            placeholder: "Search for units...",
            label: "Available Units",
            icon: "⚔️",
            badgeColor: "bg-amber-100 text-amber-800",
            badgeText: "Unit",
            displayName: (item: UnitItem) => item.displayName,
            itemKey: (item: UnitItem) => item.displayName,
            iconUrl: (item: UnitItem) => item.iconUrl
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
                config={unitConfig}
                isLoading={isLoading}
            />
        );
    }
);

UnitSearchBox.displayName = 'UnitSearchBox';