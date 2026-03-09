import {forwardRef, JSX} from 'react';
import { motion } from 'framer-motion';
import { JumpingDots } from "../JumpingDots.tsx";

interface GenericSearchPanelProps<T> {
    searchQuery: string;
    searchResultItems: T[];
    selectedItems: T[];
    onSearchChange: (query: string) => void;
    isLoading: boolean;
    onAddSelectedItem: (item: T) => void;
    onRemoveSelectedItem: (itemId: string) => void;
    // Render functions for customization
    renderSearchItem: (item: T) => JSX.Element;
    renderSelectedItem: (item: T, onRemove: () => void) => JSX.Element;
    getItemKey: (item: T) => string;
    placeholder: string;
    helpText: string;
}

export const GenericPopupSearchPanel = <T,>({
    searchQuery,
    searchResultItems,
    selectedItems,
    onSearchChange,
    isLoading,
    onAddSelectedItem,
    onRemoveSelectedItem,
    renderSearchItem,
    renderSelectedItem,
    getItemKey,
    placeholder,
    helpText
}: GenericSearchPanelProps<T>, ref: React.Ref<HTMLDivElement>) => {
    return (
        <div className="absolute bottom-5 left-0 right-0 flex justify-center z-[1000] pointer-events-none">
            <motion.div
                ref={ref}
                initial={{ opacity: 0, y: 50 }}
                animate={{ opacity: 1, y: 0 }}
                className="bg-primary border-2 border-primary rounded-xl p-5 w-[500px] max-w-[90vw] max-h-[400px] shadow-lg backdrop-blur-md pointer-events-auto"
            >
                {/* Search Input */}
                <div className="mb-4">
                    <input
                        type="text"
                        placeholder={placeholder}
                        value={searchQuery}
                        onChange={(e) => onSearchChange(e.target.value)}
                        className="w-full p-3 border-2 border-main rounded-lg text-base text-main bg-white outline-none focus:border-focus theme-focus"
                    />
                </div>

                {/* Selected Items */}
                {selectedItems.length > 0 && (
                    <div className="mb-4">
                        <h4 className="m-0 mb-2 text-sm font-bold text-main">
                            Selected Filters:
                        </h4>
                        <div className="flex flex-wrap gap-2">
                            {selectedItems.map((item) => (
                                <motion.div
                                    key={getItemKey(item)}
                                    initial={{ opacity: 0, scale: 0.8 }}
                                    animate={{ opacity: 1, scale: 1 }}
                                    exit={{ opacity: 0, scale: 0.8 }}
                                >
                                    {renderSelectedItem(item, () => onRemoveSelectedItem(getItemKey(item)))}
                                </motion.div>
                            ))}
                        </div>
                    </div>
                )}

                {/* Loading State */}
                {isLoading && (
                    <div className="min-h-[75px] relative flex flex-col items-center justify-center">
                        <JumpingDots />
                    </div>
                )}

                {/* Search Results */}
                {!isLoading && searchResultItems.length > 0 && (
                    <div className="max-h-[200px] overflow-y-auto border border-gray-200 rounded-lg theme-scrollbar">
                        {searchResultItems.map((result) => (
                            <motion.div
                                key={getItemKey(result)}
                                initial={{ opacity: 0, x: -20 }}
                                animate={{ opacity: 1, x: 0 }}
                                className="cursor-pointer border-b border-gray-100 last:border-b-0 hover:bg-surface-hover"
                                onClick={() => onAddSelectedItem(result)}
                            >
                                {renderSearchItem(result)}
                            </motion.div>
                        ))}
                    </div>
                )}

                {/* No Results State */}
                {!isLoading && searchResultItems.length === 0 && searchQuery.length > 0 && (
                    <div className="p-3 text-center text-secondary">
                        No results found
                    </div>
                )}

                {/* Help Text */}
                <div className="mt-3 text-xs text-secondary text-center italic">
                    {helpText}
                </div>
            </motion.div>
        </div>
    );
};

// Forward ref with generics requires this approach
export const GenericSearchPanelWithRef = forwardRef(GenericPopupSearchPanel) as <T>(
    props: GenericSearchPanelProps<T> & { ref?: React.Ref<HTMLDivElement> }
) => JSX.Element;
