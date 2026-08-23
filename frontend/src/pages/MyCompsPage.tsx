import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export const MyCompsPage: React.FC = () => {
    const { user, isLoading } = useAuth();

    if (isLoading) {
        return null;
    }

    if (!user) {
        return (
            <div className="flex justify-center items-start pt-24 min-h-screen">
                <div className="text-center">
                    <p className="text-secondary mb-4">You need to be logged in to view your comps.</p>
                    <Link to="/login" className="text-accent font-medium">Log in</Link>
                </div>
            </div>
        );
    }

    return (
        <div className="p-8 max-w-2xl mx-auto min-h-screen">
            <h1 className="text-3xl font-bold text-primary mb-4">My Comps</h1>
            <p className="text-secondary">Coming soon — saving and browsing your compositions will live here.</p>
        </div>
    );
};
