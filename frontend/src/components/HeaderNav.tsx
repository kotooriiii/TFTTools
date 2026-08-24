import React, {useEffect, useRef, useState} from 'react';
import {getToolsForNav} from '../config/RouteConfig';

interface HeaderNavProps
{
    currentPath: string;
    onNavigate: (path: string) => void;
}

export const HeaderNav: React.FC<HeaderNavProps> = ({currentPath, onNavigate}) =>
{
    const tools = getToolsForNav();
    const [isOpen, setIsOpen] = useState(false);
    const menuRef = useRef<HTMLDivElement>(null);

    useEffect(() =>
    {
        const handleClickOutside = (e: MouseEvent) =>
        {
            if (menuRef.current && !menuRef.current.contains(e.target as Node))
            {
                setIsOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const handleSelect = (path: string) =>
    {
        setIsOpen(false);
        onNavigate(path);
    };

    return (
        <nav className="flex items-center min-w-0">
            {/* Desktop: horizontal pills */}
            <div className="hidden lg:flex items-center gap-1">
                {tools.map((tool) =>
                {
                    const isActive = currentPath === tool.path;

                    return (
                        <div
                            key={tool.id}
                            onClick={() => onNavigate(tool.path)}
                            className={`
                                h-9 px-3 rounded-md flex items-center cursor-pointer transition-all duration-150
                                ${isActive
                                    ? 'bg-secondary text-primary'
                                    : 'bg-transparent text-secondary hover:bg-accent hover:text-primary'
                                }
                            `}
                        >
                            <span className="text-base mr-2">{tool.icon}</span>
                            <span className="text-sm font-medium whitespace-nowrap">{tool.name}</span>
                        </div>
                    );
                })}
            </div>

            {/* Narrow viewports: hamburger + dropdown */}
            <div ref={menuRef} className="relative lg:hidden">
                <button
                    onClick={() => setIsOpen((open) => !open)}
                    aria-label="Toggle navigation menu"
                    className="w-9 h-9 rounded-md flex items-center justify-center cursor-pointer text-secondary text-lg transition-all duration-150 hover:bg-accent hover:text-primary"
                >
                    ☰
                </button>

                {isOpen && (
                    <div className="absolute left-0 top-11 w-56 bg-primary border border-border rounded-lg shadow-md py-1 z-50">
                        {tools.map((tool) =>
                        {
                            const isActive = currentPath === tool.path;

                            return (
                                <div
                                    key={tool.id}
                                    onClick={() => handleSelect(tool.path)}
                                    className={`
                                        px-4 py-2 flex items-center gap-2 cursor-pointer text-sm
                                        ${isActive
                                            ? 'bg-secondary text-primary'
                                            : 'text-primary hover:bg-accent'
                                        }
                                    `}
                                >
                                    <span>{tool.icon}</span>
                                    <span>{tool.name}</span>
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>
        </nav>
    );
};
