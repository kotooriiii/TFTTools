import React from 'react';
import { Tool } from '../types/toolTypes';

interface ToolContainerProps {
  activeTool: Tool | undefined;
}

export const ToolContainer: React.FC<ToolContainerProps> = ({ activeTool }) => {
  if (!activeTool) {
    return (
      <div className="flex-1 flex items-center justify-center bg-gray-50">
        <div className="text-center text-gray-500">
          <div className="text-6xl mb-4">🛠️</div>
          <h2 className="text-2xl font-semibold mb-2">Welcome to Multi-Tool</h2>
          <p>Select a tool from the sidebar to get started</p>
        </div>
      </div>
    );
  }

  const ToolComponent = activeTool.component;
  
  return (
    <div className="flex-1 overflow-hidden">
      <ToolComponent />
    </div>
  );
};