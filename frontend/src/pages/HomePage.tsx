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
    <div className="p-8 max-w-6xl mx-auto">
      {/* Hero Section */}
      <div className="mb-12 text-center">
        <h1 className="text-4xl font-bold text-stone-800 mb-4">
          Welcome to TFT Tools
        </h1>
        <p className="text-xl text-stone-600 mb-8">
          Your comprehensive toolkit for Teamfight Tactics analysis and visualization
        </p>
        <div className="w-24 h-1 bg-stone-400 mx-auto"></div>
      </div>

      {/* Rest of the HomePage component stays the same... */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
        <div className="bg-white rounded-lg shadow-md p-6 text-center">
          <div className="text-3xl font-bold text-stone-800 mb-2">{toolsOnly.length}</div>
          <div className="text-stone-600">Available Tools</div>
        </div>
        <div className="bg-white rounded-lg shadow-md p-6 text-center">
          <div className="text-3xl font-bold text-stone-800 mb-2">∞</div>
          <div className="text-stone-600">Possibilities</div>
        </div>
        <div className="bg-white rounded-lg shadow-md p-6 text-center">
          <div className="text-3xl font-bold text-stone-800 mb-2">🚀</div>
          <div className="text-stone-600">Ready to Launch</div>
        </div>
      </div>

      {/* Tools Grid */}
      <div className="mb-12">
        <h2 className="text-2xl font-bold text-stone-800 mb-6">Available Tools</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {toolsOnly.map((tool) => (
            <div
              key={tool.id}
              onClick={() => handleToolSelect(tool)}
              className="bg-white rounded-lg shadow-md hover:shadow-lg transition-all duration-200 cursor-pointer group border border-stone-200 hover:border-stone-300"
            >
              <div className="p-6">
                <div className="flex items-center mb-4">
                  <span className="text-3xl mr-3 group-hover:scale-110 transition-transform duration-200">
                    {tool.icon}
                  </span>
                  <h3 className="text-xl font-semibold text-stone-800 group-hover:text-stone-900">
                    {tool.name}
                  </h3>
                </div>
                <p className="text-stone-600 mb-4">
                  {tool.description}
                </p>
                <div className="text-stone-500 text-sm font-medium group-hover:text-stone-600">
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