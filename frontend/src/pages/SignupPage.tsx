import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useGoogleLogin } from '@react-oauth/google';
import { useAuth } from '../contexts/AuthContext';
import { AuthApiError } from '../services/authService';
import { GoogleIcon } from '../components/GoogleIcon';

export const SignupPage: React.FC = () => {
    const { signup, loginWithGoogle } = useAuth();
    const navigate = useNavigate();

    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setFieldErrors({});
        setIsSubmitting(true);

        try {
            await signup(username, email, password);
            navigate('/');
        } catch (err) {
            if (err instanceof AuthApiError) {
                setError(err.fieldErrors ? null : err.message);
                setFieldErrors(err.fieldErrors ?? {});
            } else {
                setError('Signup failed');
            }
        } finally {
            setIsSubmitting(false);
        }
    };

    const googleSignup = useGoogleLogin({
        flow: 'auth-code',
        onSuccess: async (response) => {
            setError(null);
            setFieldErrors({});
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
                <h1 className="text-2xl font-bold text-primary mb-6 text-center">Sign Up</h1>

                <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                    <div>
                        <label className="block text-sm font-medium text-secondary mb-1">Username</label>
                        <input
                            type="text"
                            required
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="w-full p-2.5 border border-border rounded-lg bg-primary text-primary text-sm outline-none focus:ring-2 focus:ring-border"
                        />
                        {fieldErrors.username && <div className="text-error text-xs mt-1">{fieldErrors.username}</div>}
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-secondary mb-1">Email</label>
                        <input
                            type="email"
                            required
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full p-2.5 border border-border rounded-lg bg-primary text-primary text-sm outline-none focus:ring-2 focus:ring-border"
                        />
                        {fieldErrors.email && <div className="text-error text-xs mt-1">{fieldErrors.email}</div>}
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-secondary mb-1">Password</label>
                        <input
                            type="password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full p-2.5 border border-border rounded-lg bg-primary text-primary text-sm outline-none focus:ring-2 focus:ring-border"
                        />
                        {fieldErrors.password && <div className="text-error text-xs mt-1">{fieldErrors.password}</div>}
                    </div>

                    {error && <div className="text-error text-sm">{error}</div>}

                    <button
                        type="submit"
                        disabled={isSubmitting}
                        className="mt-2 bg-secondary text-primary rounded-lg py-2.5 font-medium cursor-pointer hover:opacity-90 transition-opacity disabled:opacity-50"
                    >
                        {isSubmitting ? 'Signing up...' : 'Sign Up'}
                    </button>

                    <div className="flex items-center gap-3">
                        <div className="flex-1 h-px bg-border" />
                        <span className="text-xs text-secondary">or</span>
                        <div className="flex-1 h-px bg-border" />
                    </div>

                    <button
                        type="button"
                        onClick={() => googleSignup()}
                        className="flex items-center justify-center gap-2 border border-border rounded-lg py-2.5 font-medium cursor-pointer hover:bg-accent/10 transition-colors"
                    >
                        <GoogleIcon />
                        Continue with Google
                    </button>
                </form>

                <p className="text-sm text-secondary text-center mt-6">
                    Already have an account? <Link to="/login" className="text-accent font-medium">Log in</Link>
                </p>
            </div>
        </div>
    );
};
