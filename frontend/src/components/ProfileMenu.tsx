import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export const ProfileMenu: React.FC = () => {
    const { user, isLoading, logout } = useAuth();
    const navigate = useNavigate();
    const [isOpen, setIsOpen] = useState(false);
    const menuRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const handleClickOutside = (e: MouseEvent) => {
            if (menuRef.current && !menuRef.current.contains(e.target as Node)) {
                setIsOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    if (isLoading) {
        return null;
    }

    if (!user) {
        return (
            <button
                onClick={() => navigate('/login')}
                className="px-4 py-1.5 bg-secondary text-primary rounded-lg text-sm font-medium cursor-pointer hover:opacity-90 transition-opacity"
            >
                Login
            </button>
        );
    }

    const initials = user.username.slice(0, 2).toUpperCase();

    const handleNavigate = (path: string) => {
        setIsOpen(false);
        navigate(path);
    };

    const handleLogout = () => {
        setIsOpen(false);
        logout();
        navigate('/');
    };

    return (
        <div ref={menuRef} className="relative">
            <button
                onClick={() => setIsOpen((open) => !open)}
                className="w-8 h-8 rounded-full bg-accent text-primary flex items-center justify-center text-xs font-semibold cursor-pointer"
            >
                {initials}
            </button>

            {isOpen && (
                <div className="absolute right-0 top-10 w-44 bg-primary border border-border rounded-lg shadow-md py-1 z-50">
                    <button
                        onClick={() => handleNavigate('/profile')}
                        className="w-full text-left px-4 py-2 text-sm text-primary hover:bg-accent cursor-pointer"
                    >
                        Profile
                    </button>
                    <button
                        onClick={() => handleNavigate('/my-comps')}
                        className="w-full text-left px-4 py-2 text-sm text-primary hover:bg-accent cursor-pointer"
                    >
                        My Comps
                    </button>
                    <button
                        onClick={() => handleNavigate('/settings')}
                        className="w-full text-left px-4 py-2 text-sm text-primary hover:bg-accent cursor-pointer"
                    >
                        Settings
                    </button>
                    <div className="border-t border-border my-1" />
                    <button
                        onClick={handleLogout}
                        className="w-full text-left px-4 py-2 text-sm text-primary hover:bg-accent cursor-pointer"
                    >
                        Logout
                    </button>
                </div>
            )}
        </div>
    );
};
