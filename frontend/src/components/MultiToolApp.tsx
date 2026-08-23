import React, {useEffect, useRef} from 'react';
import {Route, Routes, useLocation, useNavigate} from 'react-router-dom';
import {Sidebar} from './Sidebar';
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
            <header className="h-14 flex-shrink-0 border-b border-border bg-primary flex items-center justify-end px-4">
                <ProfileMenu/>
            </header>

            <div className="flex-1 flex overflow-hidden">
                <Sidebar
                    currentPath={location.pathname}
                    onNavigate={handleNavigate}
                />
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
        </div>
    );
};