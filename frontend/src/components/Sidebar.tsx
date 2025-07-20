import React from 'react';
import { Tool } from '../types/toolTypes';

interface SidebarProps {
  tools: Tool[];
  activeTool: string;
  onToolSelect: (toolId: string) => void;
  isCollapsed: boolean;
  onToggleCollapse: () => void;
}

export const Sidebar: React.FC<SidebarProps> = ({
  tools,
  activeTool,
  onToolSelect,
  isCollapsed,
  onToggleCollapse
}) => {
  return (
    <div className={`bg-gray-800 text-white transition-all duration-300 flex flex-col ${
      isCollapsed ? 'w-16' : 'w-64'
    }`}>
      {/* Header */}
      <div className="flex items-center justify-between p-4 border-b border-gray-700">
        {!isCollapsed && (
          <h2 className="text-lg font-semibold">Tools</h2>
        )}
        <button
          onClick={onToggleCollapse}
          className="p-2 rounded-lg hover:bg-gray-700 transition-colors"
          aria-label={isCollapsed ? "Expand sidebar" : "Collapse sidebar"}
        >
          {isCollapsed ? '→' : '←'}
        </button>
      </div>

      {/* Navigation */}
      <nav className="flex-1 p-2">
        <ul className="space-y-1">
          {tools.map((tool) => (
            <li key={tool.id}>
              <button
                onClick={() => onToolSelect(tool.id)}
                className={`w-full flex items-center p-3 rounded-lg transition-colors text-left ${
                  activeTool === tool.id
                    ? 'bg-blue-600 text-white'
                    : 'hover:bg-gray-700 text-gray-300'
                }`}
                title={isCollapsed ? tool.name : undefined}
              >
                <span className="text-xl mr-3 flex-shrink-0">
                  {tool.icon}
                </span>
                {!isCollapsed && (
                  <div>
                    <div className="font-medium">{tool.name}</div>
                    {tool.description && (
                      <div className="text-xs text-gray-400 mt-1">
                        {tool.description}
                      </div>
                    )}
                  </div>
                )}
              </button>
            </li>
          ))}
        </ul>
      </nav>

      {/* Footer */}
      <div className="p-4 border-t border-gray-700">
        {!isCollapsed && (
          <div className="text-xs text-gray-400">
            Multi-Tool Application
          </div>
        )}
      </div>
    </div>
  );
};