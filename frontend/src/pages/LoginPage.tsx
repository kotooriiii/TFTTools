import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useGoogleLogin } from '@react-oauth/google';
import { useAuth } from '../contexts/AuthContext';
import { AuthApiError } from '../services/authService';

export const LoginPage: React.FC = () => {
    const { login, loginWithGoogle } = useAuth();
    const navigate = useNavigate();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setIsSubmitting(true);

        try {
            await login(email, password);
            navigate('/');
        } catch (err) {
            setError(err instanceof AuthApiError ? err.message : 'Login failed');
        } finally {
            setIsSubmitting(false);
        }
    };

    const googleLogin = useGoogleLogin({
        flow: 'auth-code',
        onSuccess: async (response) => {
            setError(null);
            try {
                await loginWithGoogle(response.code);
                navigate('/');
            } catch (err) {
                setError(err instanceof AuthApiError ? err.message : 'Google sign-in failed');
            }
        },
        onError: () => setError('Google sign-in failed'),
    });

    return (
        <div className="flex justify-center items-start pt-24 min-h-screen">
            <div className="w-full max-w-sm bg-primary border border-border rounded-lg shadow-md p-8">
                <h1 className="text-2xl font-bold text-primary mb-6 text-center">Log In</h1>

                <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                    <div>
                        <label className="block text-sm font-medium text-secondary mb-1">Email</label>
                        <input
                            type="email"
                            required
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full p-2.5 border border-border rounded-lg bg-white text-sm outline-none focus:ring-2 focus:ring-accent"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-secondary mb-1">Password</label>
                        <input
                            type="password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full p-2.5 border border-border rounded-lg bg-white text-sm outline-none focus:ring-2 focus:ring-accent"
                        />
                    </div>

                    {error && <div className="text-error text-sm">{error}</div>}

                    <button
                        type="submit"
                        disabled={isSubmitting}
                        className="mt-2 bg-secondary text-primary rounded-lg py-2.5 font-medium cursor-pointer hover:opacity-90 transition-opacity disabled:opacity-50"
                    >
                        {isSubmitting ? 'Logging in...' : 'Log In'}
                    </button>

                    <div className="flex items-center gap-3">
                        <div className="flex-1 h-px bg-border" />
                        <span className="text-xs text-secondary">or</span>
                        <div className="flex-1 h-px bg-border" />
                    </div>

                    <button
                        type="button"
                        onClick={() => googleLogin()}
                        className="border border-border rounded-lg py-2.5 font-medium cursor-pointer hover:bg-accent/10 transition-colors"
                    >
                        Continue with Google
                    </button>
                </form>

                <p className="text-sm text-secondary text-center mt-6">
                    Don't have an account? <Link to="/signup" className="text-accent font-medium">Sign up</Link>
                </p>
            </div>
        </div>
    );
};
