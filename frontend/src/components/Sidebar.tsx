import React, {useState} from 'react';
import {motion} from 'framer-motion';
import {getToolsForSidebar} from '../config/RouteConfig';

interface SidebarProps
{
    currentPath: string;
    onNavigate: (path: string) => void;
}

export const Sidebar: React.FC<SidebarProps> = ({currentPath, onNavigate}) =>
{
    const [isCollapsed, setIsCollapsed] = useState(false);
    const tools = getToolsForSidebar();

    return (
        <motion.div 
            animate={{width: isCollapsed ? 48 : 200}}
            transition={{duration: 0.2, ease: "easeInOut"}}
            className="h-screen bg-primary border-r border-border flex flex-col"
        >
            {/* Header */}
            <div className={`
                h-15 flex items-center border-b border-border bg-primary
                ${isCollapsed ? 'justify-center px-0' : 'justify-start px-4'}
            `}>
                {!isCollapsed && (
                    <motion.span
                        initial={{opacity: 0}}
                        animate={{opacity: 1}}
                        transition={{delay: 0.1}}
                        className="text-base font-semibold text-primary"
                    >
                        Tools
                    </motion.span>
                )}
                {isCollapsed && (
                    <div className="w-6 h-6 bg-accent rounded flex items-center justify-center text-xs text-white font-semibold">
                        T
                    </div>
                )}
            </div>

            {/* Navigation */}
            <nav className="bg-white-500 flex-1 py-2 flex flex-col">
                {tools.map((tool) =>
                {
                    const isActive = currentPath === tool.path;

                    return (
                        <motion.div
                            key={tool.id}
                            onClick={() => onNavigate(tool.path)}
                            className={`
                                h-10 mx-2 my-0.5 rounded-md flex items-center cursor-pointer transition-all duration-150
                                ${isCollapsed ? 'justify-center px-0' : 'justify-start px-3'}
                                ${isActive 
                                    ? 'bg-secondary text-primary' 
                                    : 'bg-transparent text-secondary hover:bg-accent hover:text-primary'
                                }
                            `}
                        >
                            <span className="text-base min-w-4 flex items-center justify-center">
                                {tool.icon}
                            </span>

                            {!isCollapsed && (
                                <motion.span
                                    initial={{opacity: 0, x: -10}}
                                    animate={{opacity: 1, x: 0}}
                                    transition={{delay: 0.05}}
                                    className="ml-3 text-sm font-medium"
                                >
                                    {tool.name}
                                </motion.span>
                            )}
                        </motion.div>
                    );
                })}
            </nav>

            {/* Toggle Button */}
            <div className="h-12 flex items-center justify-center border-t border-border">
                <motion.button
                    onClick={() => setIsCollapsed(!isCollapsed)}
                    whileHover={{scale: 1.05}}
                    whileTap={{scale: 0.95}}
                    className="w-7 h-7 border-none bg-transparent rounded cursor-pointer flex items-center justify-center text-secondary text-sm transition-all duration-150 hover:bg-secondary hover:text-primary"
                >
                    <motion.span
                        animate={{rotate: isCollapsed ? 0 : 180}}
                        transition={{duration: 0.2}}
                    >
                        ←
                    </motion.span>
                </motion.button>
            </div>
        </motion.div>
    );
};