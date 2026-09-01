import { forwardRef } from 'react';
import { SearchItem, SelectedItem } from '../../types/searchTypes';
import { GenericSearchPanelWithRef } from './GenericPopupSearchPanel.tsx';
import { Button } from '../Button';

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
                {item.type === 'unit' ? '🗡️' : '⭐'}
            </span>
            <div>
                <div className="font-bold text-primary text-sm">
                    {item.displayName}
                </div>
                <div className="text-xs text-secondary capitalize">
                    {item.type}
                </div>
            </div>
        </div>
    );

    const renderSelectedItem = (item: SelectedItem, onRemove: () => void) => (
        <div className={`flex items-center gap-1.5 text-primary px-2.5 py-1.5 rounded-2xl text-xs font-bold ${
            item.type === 'unit' ? 'bg-secondary' : 'bg-accent'
        }`}>
            <span>{item.type === 'unit' ? '🗡️' : '⭐'}</span>
            <span>{item.displayName}</span>
            <Button
                variant="ghost"
                tone="accent"
                onClick={onRemove}
                className="border-none text-primary text-sm p-0 ml-1 rounded"
            >
                ×
            </Button>
        </div>
    );

    return (
        <GenericSearchPanelWithRef
            {...props}
            ref={ref}
            renderSearchItem={renderSearchItem}
            renderSelectedItem={renderSelectedItem}
            getItemKey={(item) => item.apiName}
            placeholder="Search units or traits..."
            helpText="Search for units or traits"
        />
    );
});

UnitPopupSearchPanel.displayName = 'UnitPopupSearchPanel';
