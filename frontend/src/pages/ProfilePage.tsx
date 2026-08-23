import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export const ProfilePage: React.FC = () => {
    const { user, isLoading } = useAuth();

    if (isLoading) {
        return null;
    }

    if (!user) {
        return (
            <div className="flex justify-center items-start pt-24 min-h-screen">
                <div className="text-center">
                    <p className="text-secondary mb-4">You need to be logged in to view your profile.</p>
                    <Link to="/login" className="text-accent font-medium">Log in</Link>
                </div>
            </div>
        );
    }

    return (
        <div className="p-8 max-w-2xl mx-auto min-h-screen">
            <h1 className="text-3xl font-bold text-primary mb-8">Profile</h1>

            <div className="bg-primary border border-border rounded-lg shadow-md p-6 flex flex-col gap-4">
                <div>
                    <div className="text-xs uppercase text-secondary font-medium">Username</div>
                    <div className="text-lg text-primary">{user.username}</div>
                </div>
                <div>
                    <div className="text-xs uppercase text-secondary font-medium">Email</div>
                    <div className="text-lg text-primary">{user.email}</div>
                </div>
            </div>
        </div>
    );
};
