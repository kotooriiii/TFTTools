import { forwardRef } from 'react';
import { SearchItem, SelectedItem } from '../../types/searchTypes';
import { GenericSearchPanelWithRef } from './GenericPopupSearchPanel.tsx';

interface UnitSearchPanelProps {
    searchQuery: string;
    searchResultItems: SearchItem[];
    selectedItems: SelectedItem[];
    onSearchChange: (query: string) => void;
    isLoading: boolean;
    onAddSelectedItem: (item: SearchItem) => void;
    onRemoveSelectedItem: (itemId: string) => void;
}

export const UnitPopupSearchPanel = forwardRef<HTMLDivElement, UnitSearchPanelProps>((props, ref) => {
    const renderSearchItem = (item: SearchItem) => (
        <div className="p-3 flex items-center gap-3">
            <span className="text-base">
                {item.type === 'champion' ? '🗡️' : '⭐'}
            </span>
            <div>
                <div className="font-bold text-main text-sm">
                    {item.displayName}
                </div>
                <div className="text-xs text-secondary capitalize">
                    {item.type}
                </div>
            </div>
        </div>
    );

    const renderSelectedItem = (item: SelectedItem, onRemove: () => void) => (
        <div className={`flex items-center gap-1.5 text-white px-2.5 py-1.5 rounded-2xl text-xs font-bold ${
            item.type === 'champion' ? 'bg-champion-bg' : 'bg-trait-bg'
        }`}>
            <span>{item.type === 'champion' ? '🗡️' : '⭐'}</span>
            <span>{item.name}</span>
            <button
                onClick={onRemove}
                className="bg-transparent border-none text-white cursor-pointer text-sm p-0 ml-1 hover:opacity-70 transition-opacity"
            >
                ×
            </button>
        </div>
    );

    return (
        <GenericSearchPanelWithRef
            {...props}
            ref={ref}
            renderSearchItem={renderSearchItem}
            renderSelectedItem={renderSelectedItem}
            getItemKey={(item) => item.name}
            placeholder="Search champions or traits..."
            helpText="Search for champions or traits to filter units"
        />
    );
});

UnitPopupSearchPanel.displayName = 'UnitPopupSearchPanel';
