import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { Button } from './Button';

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
            <Button
                onClick={() => navigate('/login')}
                className="px-4 py-1.5 rounded-lg text-sm font-medium"
            >
                Login
            </Button>
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
            <Button
                tone="accent"
                onClick={() => setIsOpen((open) => !open)}
                className="w-8 h-8 rounded-full flex items-center justify-center text-xs font-semibold"
            >
                {initials}
            </Button>

            {isOpen && (
                <div className="absolute right-0 top-10 w-44 bg-primary border border-border rounded-lg shadow-md py-1 z-50">
                    <Button
                        variant="ghost"
                        tone="accent"
                        onClick={() => handleNavigate('/profile')}
                        className="w-full text-left px-4 py-2 text-sm text-primary"
                    >
                        Profile
                    </Button>
                    <Button
                        variant="ghost"
                        tone="accent"
                        onClick={() => handleNavigate('/my-comps')}
                        className="w-full text-left px-4 py-2 text-sm text-primary"
                    >
                        My Comps
                    </Button>
                    <Button
                        variant="ghost"
                        tone="accent"
                        onClick={() => handleNavigate('/settings')}
                        className="w-full text-left px-4 py-2 text-sm text-primary"
                    >
                        Settings
                    </Button>
                    <div className="border-t border-border my-1" />
                    <Button
                        variant="ghost"
                        tone="accent"
                        onClick={handleLogout}
                        className="w-full text-left px-4 py-2 text-sm text-primary"
                    >
                        Logout
                    </Button>
                </div>
            )}
        </div>
    );
};
