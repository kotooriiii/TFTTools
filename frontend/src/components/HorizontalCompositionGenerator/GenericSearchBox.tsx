// components/search/GenericSearchBox.tsx
import {motion} from 'framer-motion';
import {JumpingDots} from "../JumpingDots.tsx";

interface SearchConfig<T>
{
    placeholder: string;
    label: string;
    icon: string;
    badgeColor: string;
    badgeText: string;
    displayName: (item: T) => string;
    itemKey: (item: T) => string;
}

interface GenericSearchBoxProps<T>
{
    searchQuery: string;
    searchResults: T[];
    selectedItems: T[];
    onSearchChange: (query: string) => void;
    onAddItem: (item: T) => void;
    onRemoveItem: (key: string) => void;
    onUpdateCount?: (key: string, count: number) => void;
    config: SearchConfig<T>;
    showCount?: boolean;
    getItemCount?: (item: T) => number;
    isLoading?: boolean;
    getItemAllowedCounts?: (item: T) => number[] | undefined;
}

export const GenericSearchBox = <T, >({
                                          searchQuery,
                                          searchResults,
                                          selectedItems,
                                          onSearchChange,
                                          onAddItem,
                                          onRemoveItem,
                                          onUpdateCount,
                                          config,
                                          showCount = false,
                                          getItemCount,
                                          isLoading = false,
                                          getItemAllowedCounts
                                      }: GenericSearchBoxProps<T>) =>
{
    const handleCountChange = (item: T, newValue: number) => {
        const allowedCounts = getItemAllowedCounts?.(item);
        
        if (allowedCounts && allowedCounts.length > 0) {

            let closestValue: number;
            
            // If the new value is higher than current, find next higher allowed value
            // If lower, find next lower allowed value
            const currentValue = getItemCount?.(item) || allowedCounts[0];
            
            if (newValue > currentValue) {
                // Going up - find next higher value
                closestValue = allowedCounts.find(val => val > currentValue) || allowedCounts[allowedCounts.length - 1];
            } else if (newValue < currentValue) {
                // Going down - find next lower value
                const lowerValues = allowedCounts.filter(val => val < currentValue);
                closestValue = lowerValues.length > 0 ? lowerValues[lowerValues.length - 1] : allowedCounts[0];
            } else {
                closestValue = currentValue;
            }
            
            onUpdateCount?.(config.itemKey(item), closestValue);
        } else {
            // Free count - use the value directly but constrain to 1-9
            const constrainedValue = Math.max(1, Math.min(9, newValue));
            onUpdateCount?.(config.itemKey(item), constrainedValue);
        }
    };

    return (
        <div className="mb-6">
            <label className="block text-sm font-medium text-foreground mb-2">
                {config.label}
            </label>
            <input
                type="text"
                value={searchQuery}
                onChange={(e) => onSearchChange(e.target.value)}
                className="w-full px-3 py-2 border border-border rounded-md bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                placeholder={config.placeholder}
            />

            {/* Loading State */}
            {isLoading && (
                <motion.div
                    initial={{opacity: 0, y: 10}}
                    animate={{opacity: 1, y: 0}}
                    className="mt-4 bg-card border border-border rounded-lg overflow-hidden"
                >
                    <div className="px-4 py-2 bg-accent border-b border-border">
                        <span className="text-sm font-medium text-foreground">{config.label}</span>
                    </div>
                    <div className="min-h-[120px] flex items-center justify-center p-4">
                        <JumpingDots/>
                    </div>
                </motion.div>
            )}


            {/* Search Results Table */}
            {!isLoading && searchResults.length > 0 && (
                <motion.div
                    initial={{opacity: 0, y: 10}}
                    animate={{opacity: 1, y: 0}}
                    className="mt-4 bg-card border border-border rounded-lg overflow-hidden"
                >
                    <div className="px-4 py-2 bg-accent border-b border-border">
                        <span className="text-sm font-medium text-foreground">{config.label}</span>
                    </div>
                    <div className="max-h-48 overflow-y-auto custom-scrollbar">
                        <table className="w-full">
                            <tbody>
                            {searchResults.map((item, index) => (
                                <motion.tr
                                    key={config.itemKey(item)}
                                    initial={{opacity: 0, x: -20}}
                                    animate={{opacity: 1, x: 0}}
                                    transition={{delay: index * 0.05}}
                                    onClick={() => onAddItem(item)}
                                    className="cursor-pointer hover:bg-accent/50 transition-colors border-b border-border last:border-b-0"
                                >
                                    <td className="px-4 py-3 flex items-center gap-3">
                                        <span className="text-lg">{config.icon}</span>
                                        <span className="font-medium text-foreground">
                                                {config.displayName(item)}
                                            </span>
                                    </td>
                                    <td className="px-4 py-3 text-right">
                                            <span className={`text-xs px-2 py-1 rounded-full ${config.badgeColor}`}>
                                                {config.badgeText}
                                            </span>
                                    </td>
                                </motion.tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </motion.div>
            )}

            {/* Search Results Empty Table */}
            {!isLoading && searchResults.length == 0 && searchQuery.length > 0 && (
                <motion.div
                    initial={{opacity: 0, y: 10}}
                    animate={{opacity: 1, y: 0}}
                    className="mt-4 bg-card border border-border rounded-lg overflow-hidden"
                >
                    <div className="px-4 py-2 bg-accent border-b border-border">
                        <span className="text-sm font-medium text-foreground">{config.label}</span>
                    </div>
                    <div className="max-h-48 overflow-y-auto custom-scrollbar">
                        <table className="w-full">
                            <tbody>
                            <tr>
                                <td className="px-4 py-8 text-center text-muted-foreground" colSpan={2}>
                                    No results found for "{searchQuery}"
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </motion.div>
            )}

            {/* Selected Items */}
            {selectedItems.length > 0 && (
                <div className="flex flex-wrap gap-2 mt-3">
                    {selectedItems.map((item) =>
                    {
                        const allowedCounts = getItemAllowedCounts?.(item);
                        const currentCount = getItemCount?.(item) || 1;
                        const minValue = allowedCounts && allowedCounts.length > 0 ? Math.min(...allowedCounts) : 1;
                        const maxValue = allowedCounts && allowedCounts.length > 0 ? Math.max(...allowedCounts) : 9;

                        return (
                            <motion.div
                                key={config.itemKey(item)}
                                initial={{opacity: 0, scale: 0.8}}
                                animate={{opacity: 1, scale: 1}}
                                className={`flex items-center gap-1.5 px-3 py-1.5 rounded-full text-sm font-medium ${config.badgeColor}`}
                            >
                                <span>{config.icon}</span>
                                <span>{config.displayName(item)}</span>
                                {showCount && onUpdateCount && getItemCount && (
                                    // Always use number input with up/down buttons
                                    <input
                                        type="number"
                                        min={minValue}
                                        max={maxValue}
                                        value={currentCount}
                                        onChange={(e) => handleCountChange(item, parseInt(e.target.value) || minValue)}
                                        className="w-16 px-2 py-1 text-center border border-purple-300 rounded bg-white text-purple-800"
                                    />
                                )}
                                <button
                                    onClick={() => onRemoveItem(config.itemKey(item))}
                                    className="text-current hover:text-current/80 ml-1"
                                >
                                    ×
                                </button>
                            </motion.div>
                        )
                    })}
                </div>
            )}
        </div>
    );
};