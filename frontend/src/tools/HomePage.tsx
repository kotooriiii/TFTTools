import React from 'react';
import { Tool } from '../types/toolTypes';

interface HomePageProps {
  tools: Tool[];
  onToolSelect: (toolId: string) => void;
}

const HomePage: React.FC<HomePageProps> = ({ tools, onToolSelect }) => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-8">
      <div className="max-w-6xl mx-auto">
        {/* Header Section */}
        <div className="text-center mb-12">
          <h1 className="text-5xl font-bold text-gray-800 mb-4">
            Multi-Tool Dashboard
          </h1>
          <p className="text-xl text-gray-600 max-w-2xl mx-auto">
            A powerful suite of interactive tools for data visualization, 
            analysis, and exploration. Choose a tool below to get started.
          </p>
        </div>

        {/* Tools Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 mb-12">
          {tools.map((tool) => (
            <div
              key={tool.id}
              onClick={() => onToolSelect(tool.id)}
              className="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 cursor-pointer transform hover:-translate-y-2 border border-gray-200 overflow-hidden group"
            >
              <div className="p-8 text-center">
                <div className="text-6xl mb-4 group-hover:scale-110 transition-transform duration-300">
                  {tool.icon}
                </div>
                <h3 className="text-2xl font-semibold text-gray-800 mb-3">
                  {tool.name}
                </h3>
                <p className="text-gray-600 text-sm leading-relaxed">
                  {tool.description || `Explore and interact with ${tool.name.toLowerCase()}`}
                </p>
              </div>
              <div className="bg-gradient-to-r from-blue-500 to-indigo-600 h-1 w-0 group-hover:w-full transition-all duration-300"></div>
            </div>
          ))}
        </div>

        {/* Features Section */}
        <div className="bg-white rounded-2xl shadow-lg p-8 mb-12">
          <h2 className="text-3xl font-bold text-gray-800 text-center mb-8">
            Platform Features
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="text-4xl mb-4">⚡</div>
              <h3 className="text-xl font-semibold mb-2">Fast & Responsive</h3>
              <p className="text-gray-600">Built with modern React and optimized for performance</p>
            </div>
            <div className="text-center">
              <div className="text-4xl mb-4">🎨</div>
              <h3 className="text-xl font-semibold mb-2">Interactive UI</h3>
              <p className="text-gray-600">Intuitive interfaces with smooth animations and feedback</p>
            </div>
            <div className="text-center">
              <div className="text-4xl mb-4">🔧</div>
              <h3 className="text-xl font-semibold mb-2">Modular Tools</h3>
              <p className="text-gray-600">Each tool is purpose-built for specific tasks and workflows</p>
            </div>
          </div>
        </div>

        {/* Quick Start Section */}
        <div className="bg-gradient-to-r from-blue-500 to-indigo-600 rounded-2xl p-8 text-white text-center">
          <h2 className="text-3xl font-bold mb-4">Ready to Get Started?</h2>
          <p className="text-xl mb-6 opacity-90">
            Select any tool above to begin exploring your data and workflows.
          </p>
          <div className="flex justify-center space-x-4">
            <div className="bg-white bg-opacity-20 rounded-lg px-4 py-2">
              <span className="text-sm">💡 Tip: Use the sidebar to switch between tools anytime</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;