import React, { createContext, useContext, useState, ReactNode, useEffect } from 'react';
import { authService, UserResponse } from '../services/authService';

const TOKEN_STORAGE_KEY = 'tfttools_auth_token';

interface AuthContextType {
    user: UserResponse | null;
    token: string | null;
    isLoading: boolean;
    login: (email: string, password: string) => Promise<void>;
    signup: (username: string, email: string, password: string) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<UserResponse | null>(null);
    const [token, setToken] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    // Rehydrate session from a persisted token on load
    useEffect(() => {
        const storedToken = localStorage.getItem(TOKEN_STORAGE_KEY);

        if (!storedToken) {
            setIsLoading(false);
            return;
        }

        authService.me(storedToken)
            .then((rehydratedUser) => {
                setToken(storedToken);
                setUser(rehydratedUser);
            })
            .catch(() => {
                localStorage.removeItem(TOKEN_STORAGE_KEY);
            })
            .finally(() => setIsLoading(false));
    }, []);

    const login = async (email: string, password: string) => {
        const { token: newToken, user: newUser } = await authService.login(email, password);
        localStorage.setItem(TOKEN_STORAGE_KEY, newToken);
        setToken(newToken);
        setUser(newUser);
    };

    const signup = async (username: string, email: string, password: string) => {
        await authService.signup(username, email, password);
        await login(email, password);
    };

    const logout = () => {
        localStorage.removeItem(TOKEN_STORAGE_KEY);
        setToken(null);
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, token, isLoading, login, signup, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
