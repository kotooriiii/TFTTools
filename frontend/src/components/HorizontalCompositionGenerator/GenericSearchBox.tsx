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
                                    allowedCounts ? (
                                        // Dropdown for constrained counts
                                        <select
                                            value={getItemCount(item)}
                                            onChange={(e) => onUpdateCount(config.itemKey(item), parseInt(e.target.value))}
                                            className="w-16 px-2 py-1 text-center border border-purple-300 rounded bg-white text-purple-800"
                                        >
                                            {allowedCounts.map(count => (
                                                <option key={count} value={count}>
                                                    {count}
                                                </option>
                                            ))}
                                        </select>
                                    ) : (
                                        // Number input for free counts
                                        <input
                                            type="number"
                                            min="1"
                                            max="9"
                                            value={getItemCount(item)}
                                            onChange={(e) => onUpdateCount(config.itemKey(item), parseInt(e.target.value))}
                                            className="w-16 px-2 py-1 text-center border border-purple-300 rounded bg-white text-purple-800"
                                        />
                                    )
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