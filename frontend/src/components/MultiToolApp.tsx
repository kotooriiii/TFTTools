import React from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation, useNavigate } from 'react-router-dom';
import { Sidebar } from './Sidebar';
import { ROUTE_CONFIG } from '../config/RouteConfig';

export const MultiToolApp: React.FC = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const handleNavigate = (path: string) => {
        navigate(path);
    };

  return (
    <div className="h-screen bg-stone-50 flex">
      <Sidebar
          currentPath={location.pathname}
          onNavigate={handleNavigate}
      />
      <main className="flex-1 overflow: 'auto'">
        <Routes>
          {ROUTE_CONFIG.map(({ path, component: Component }) => (
            <Route 
              key={path} 
              path={path} 
              element={<Component />} 
            />
          ))}
        </Routes>
      </main>
    </div>
  );
};