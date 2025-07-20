import React, { useState } from 'react';
import {Sidebar} from './Sidebar';
import {ToolContainer} from './ToolContainer';
import HomePage from '../tools/HomePage';
import GraphCanvasTool from '../tools/GraphCanvasTool';
import PlaceholderTool from '../tools/PlaceholderTool';
import { Tool } from '../types/toolTypes';

const MultiToolApp: React.FC = () => {
  const [activeTool, setActiveTool] = useState<string>('home');
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);

  const tools: Tool[] = [
    {
      id: 'home',
      name: 'Home',
      icon: '🏠',
      component: () => <HomePage tools={tools.slice(1)} onToolSelect={handleToolSelect} />,
      description: 'Welcome page and tool overview'
    },
    {
      id: 'graph-canvas',
      name: 'Graph Canvas',
      icon: '🔗',
      component: GraphCanvasTool,
      description: 'Interactive graph visualization and node manipulation'
    },
    {
      id: 'data-analyzer',
      name: 'Data Analyzer',
      icon: '📊',
      component: PlaceholderTool,
      description: 'Analyze and visualize data patterns and trends'
    },
    {
      id: 'network-mapper',
      name: 'Network Mapper',
      icon: '🌐',
      component: PlaceholderTool,
      description: 'Map and explore network connections and relationships'
    },
    {
      id: 'workflow-builder',
      name: 'Workflow Builder',
      icon: '⚙️',
      component: PlaceholderTool,
      description: 'Design and automate custom workflows and processes'
    },
  ];

  const handleToolSelect = (toolId: string) => {
    setActiveTool(toolId);
  };
  const toggleSidebar = () => {
    setSidebarCollapsed(!sidebarCollapsed);
  };

  const currentTool = tools.find(tool => tool.id === activeTool);

  return (
    <div className="flex h-screen bg-gray-100">
      <Sidebar
          tools={tools}
          activeTool={activeTool}
          onToolSelect={handleToolSelect}
          isCollapsed={sidebarCollapsed}
          onToggleCollapse={toggleSidebar}
      />
      <ToolContainer activeTool={currentTool} />
    </div>
  );
};

export default MultiToolApp;