import React from 'react';
import { useNavigate } from 'react-router-dom';
import { getToolsForSidebar } from '../config/RouteConfig';

export const HomePage: React.FC = () => {
  const navigate = useNavigate();
  const allTools = getToolsForSidebar();
  const toolsOnly = allTools.filter(tool => tool.id !== 'home');

  const handleToolSelect = (tool: any) => {
    navigate(tool.path);
  };

  return (
    <div className="p-8 max-w-6xl mx-auto min-h-screen">
      {/* Hero Section */}
      <div className="mb-12 text-center">
        <h1 className="text-4xl font-bold text-primary mb-4">
          Welcome to TFT Tools
        </h1>
        <p className="text-xl text-secondary mb-8">
          Your comprehensive toolkit for Teamfight Tactics analysis and visualization
        </p>
        <div className="w-24 h-1 bg-secondary mx-auto"></div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
        <div className="bg-primary-surface rounded-lg shadow-md p-6 text-center border border-border">
          <div className="text-3xl font-bold text-primary mb-2">{toolsOnly.length}</div>
          <div className="text-secondary">Available Tools</div>
        </div>
        <div className="bg-primary-surface rounded-lg shadow-md p-6 text-center border border-border">
          <div className="text-3xl font-bold text-primary mb-2">∞</div>
          <div className="text-secondary">Possibilities</div>
        </div>
        <div className="bg-primary-surface rounded-lg shadow-md p-6 text-center border border-border">
          <div className="text-3xl font-bold text-primary mb-2">🚀</div>
          <div className="text-secondary">Ready to Launch</div>
        </div>
      </div>

      {/* Tools Grid */}
      <div className="mb-12">
        <h2 className="text-2xl font-bold text-primary mb-6">Available Tools</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {toolsOnly.map((tool) => (
            <div
              key={tool.id}
              onClick={() => handleToolSelect(tool)}
              className="bg-primary-surface rounded-lg shadow-md hover:shadow-lg transition-all duration-200 cursor-pointer group border border-border hover:border-secondary"
            >
              <div className="p-6">
                <div className="flex items-center mb-4">
                  <span className="text-3xl mr-3 group-hover:scale-110 transition-transform duration-200">
                    {tool.icon}
                  </span>
                  <h3 className="text-xl font-semibold text-primary group-hover:text-accent">
                    {tool.name}
                  </h3>
                </div>
                <p className="text-secondary mb-4">
                  {tool.description}
                </p>
                <div className="text-secondary text-sm font-medium group-hover:text-accent transition-colors duration-200">
                  Click to open →
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};