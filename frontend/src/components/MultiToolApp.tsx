import React, {useEffect, useRef} from 'react';
import {Route, Routes, useLocation, useNavigate} from 'react-router-dom';
import {HeaderNav} from './HeaderNav';
import {ProfileMenu} from './ProfileMenu';
import {ROUTE_CONFIG} from '../config/RouteConfig';

export const MultiToolApp: React.FC = () =>
{
    const location = useLocation();
    const navigate = useNavigate();
    const mainRef = useRef<HTMLElement>(null);

    const handleNavigate = (path: string) =>
    {
        navigate(path);
    };

    // Reset scroll on route change - main persists across client-side navigations
    useEffect(() =>
    {
        mainRef.current?.scrollTo(0, 0);
    }, [location.pathname]);

    return (
        <div className="h-screen bg-stone-50 flex flex-col">
            <header className="h-14 flex-shrink-0 border-b border-border bg-primary flex items-center justify-between gap-4 px-4">
                <div className="flex items-center gap-6 min-w-0">
                    <div className="flex items-center gap-2 flex-shrink-0">
                        <div className="w-7 h-7 bg-accent rounded flex items-center justify-center text-xs text-primary font-semibold">
                            T
                        </div>
                        <span className="hidden sm:inline text-base font-semibold text-primary whitespace-nowrap">
                            TFT Tools
                        </span>
                    </div>

                    <HeaderNav
                        currentPath={location.pathname}
                        onNavigate={handleNavigate}
                    />
                </div>

                <ProfileMenu/>
            </header>

            <main ref={mainRef} className="bg-primary flex-1 overflow-auto">
                <Routes>
                    {ROUTE_CONFIG.map(({path, component: Component}) => (
                        <Route
                            key={path}
                            path={path}
                            element={<Component/>}
                        />
                    ))}
                </Routes>
            </main>
        </div>
    );
};