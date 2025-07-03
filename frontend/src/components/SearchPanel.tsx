import {forwardRef} from 'react';
import {motion} from 'framer-motion';
import {SearchItem, SelectedItem} from '../types/searchTypes';
import {JumpingDots} from "./JumpingDots.tsx";

interface SearchPanelProps
{
    searchQuery: string;
    searchResultItems: SearchItem[];
    selectedItems: SelectedItem[];
    onSearchChange: (query: string) => void;
    isLoading: boolean;
    onAddSelectedItem: (item: SearchItem) => void;
    onRemoveSelectedItem: (itemId: string) => void;
}

export const SearchPanel = forwardRef<HTMLDivElement, SearchPanelProps>(({
                                                                             searchQuery,
                                                                             searchResultItems,
                                                                             selectedItems,
                                                                             onSearchChange,
                                                                             isLoading,
                                                                             onAddSelectedItem,
                                                                             onRemoveSelectedItem
                                                                         }, ref) =>
{
    return (
        <div style={{
            position: 'absolute',
            bottom: '20px',
            left: '0',
            right: '0',
            display: 'flex',
            justifyContent: 'center',
            zIndex: 1000,
            pointerEvents: 'none'
        }}>
            <motion.div
                ref={ref}
                initial={{opacity: 0, y: 50}}
                animate={{opacity: 1, y: 0}}
                style={{
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    border: '2px solid #594b42',
                    borderRadius: '12px',
                    padding: '20px',
                    width: '500px',
                    maxWidth: '90vw',
                    maxHeight: '400px',
                    boxShadow: '0 -4px 20px rgba(0, 0, 0, 0.15)',
                    backdropFilter: 'blur(10px)',
                    pointerEvents: 'auto'
                }}
            >
                {/* Search Input */}
                <div style={{marginBottom: '16px'}}>
                    <input
                        type="text"
                        placeholder="Search champions or traits..."
                        value={searchQuery}
                        onChange={(e) => onSearchChange(e.target.value)}
                        style={{
                            width: '100%',
                            padding: '12px',
                            border: '2px solid #C3A995',
                            borderRadius: '8px',
                            fontSize: '16px',
                            outline: 'none',
                            transition: 'border-color 0.2s ease'
                        }}
                        onFocus={(e) => e.target.style.borderColor = '#8B7355'}
                        onBlur={(e) => e.target.style.borderColor = '#C3A995'}
                    />
                </div>

                {/* Selected Items */}
                {selectedItems.length > 0 && (
                    <div style={{marginBottom: '16px'}}>
                        <h4 style={{
                            margin: '0 0 8px 0',
                            fontSize: '14px',
                            fontWeight: 'bold',
                            color: '#594b42'
                        }}>
                            Selected Filters:
                        </h4>
                        <div style={{
                            display: 'flex',
                            flexWrap: 'wrap',
                            gap: '8px'
                        }}>
                            {selectedItems.map((item) => (
                                <motion.div
                                    key={item.name}
                                    initial={{opacity: 0, scale: 0.8}}
                                    animate={{opacity: 1, scale: 1}}
                                    exit={{opacity: 0, scale: 0.8}}
                                    style={{
                                        display: 'flex',
                                        alignItems: 'center',
                                        gap: '6px',
                                        backgroundColor: item.type === 'champion' ? '#8B7355' : '#A68B5B',
                                        color: 'white',
                                        padding: '6px 10px',
                                        borderRadius: '16px',
                                        fontSize: '12px',
                                        fontWeight: 'bold'
                                    }}
                                >
                                    <span>{item.type === 'champion' ? '🗡️' : '⭐'}</span>
                                    <span>{item.name}</span>
                                    <button
                                        onClick={() => onRemoveSelectedItem(item.name)}
                                        style={{
                                            background: 'none',
                                            border: 'none',
                                            color: 'white',
                                            cursor: 'pointer',
                                            fontSize: '14px',
                                            padding: '0',
                                            marginLeft: '4px'
                                        }}
                                    >
                                        ×
                                    </button>
                                </motion.div>
                            ))}
                        </div>
                    </div>
                )}

                {/* Loading State */}
                {isLoading &&
                    <div
                        style={{
                            minHeight: '75px',  // Provides consistent space
                            position: 'relative',  // Important for absolute positioning of loader
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            justifyContent: 'center'
                        }}
                    >
                        <JumpingDots/>
                    </div>
                }


                {/* Search Results */}
                {!isLoading && searchResultItems.length > 0 && (
                    <div style={{
                        maxHeight: '200px',
                        overflowY: 'auto',
                        border: '1px solid #E5E5E5',
                        borderRadius: '8px'
                    }}>
                        {searchResultItems.map((result) => (
                            <motion.div
                                key={result.name}
                                initial={{opacity: 0, x: -20}}
                                animate={{opacity: 1, x: 0}}
                                style={{
                                    padding: '12px 16px',
                                    cursor: 'pointer',
                                    borderBottom: '1px solid #F0F0F0',
                                    display: 'flex',
                                    alignItems: 'center',
                                    gap: '12px',
                                    transition: 'background-color 0.2s ease'
                                }}
                                whileHover={{backgroundColor: '#F8F8F8'}}
                                onClick={() => onAddSelectedItem(result)}
                            >
                                <span style={{fontSize: '16px'}}>
                                    {result.type === 'champion' ? '🗡️' : '⭐'}
                                </span>
                                <div>
                                    <div style={{
                                        fontWeight: 'bold',
                                        color: '#594b42',
                                        fontSize: '14px'
                                    }}>
                                        {result.name}
                                    </div>
                                    <div style={{
                                        fontSize: '12px',
                                        color: '#888',
                                        textTransform: 'capitalize'
                                    }}>
                                        {result.type}
                                    </div>
                                </div>
                            </motion.div>
                        ))}
                    </div>
                )}

                {/* No Results State */}
                {!isLoading && searchResultItems.length === 0 && searchQuery.length > 0 && (
                    <div style={{
                        padding: '12px',
                        textAlign: 'center',
                        color: '#888'
                    }}>
                        No results found
                    </div>
                )}


                {/* Help Text */}
                <div style={{
                    marginTop: '12px',
                    fontSize: '11px',
                    color: '#888',
                    textAlign: 'center',
                    fontStyle: 'italic'
                }}>
                    Search for champions or traits to filter units
                </div>
            </motion.div>
        </div>
    );
});

SearchPanel.displayName = 'SearchPanel';