import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { getToolsForSidebar } from '../config/RouteConfig';

interface SidebarProps {
  currentPath: string;
  onNavigate: (path: string) => void;
}

export const Sidebar: React.FC<SidebarProps> = ({ currentPath, onNavigate }) => {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const tools = getToolsForSidebar();

  return (
    <motion.div
      animate={{ width: isCollapsed ? 48 : 200 }}
      transition={{ duration: 0.2, ease: "easeInOut" }}
      style={{
        height: '100vh',
        backgroundColor: '#f8f9fa',
        borderRight: '1px solid #e9ecef',
        display: 'flex',
        flexDirection: 'column'
      }}
    >
      {/* Header */}
      <div style={{
        height: '60px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: isCollapsed ? 'center' : 'flex-start',
        padding: isCollapsed ? '0' : '0 16px',
        borderBottom: '1px solid #e9ecef'
      }}>
        {!isCollapsed && (
          <motion.span
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.1 }}
            style={{
              fontSize: '16px',
              fontWeight: '600',
              color: '#495057'
            }}
          >
            Tools
          </motion.span>
        )}
        {isCollapsed && (
          <div style={{
            width: '24px',
            height: '24px',
            backgroundColor: '#6c757d',
            borderRadius: '4px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontSize: '12px',
            color: 'white',
            fontWeight: '600'
          }}>
            T
          </div>
        )}
      </div>

      {/* Navigation */}
      <nav style={{
        flex: 1,
        padding: '8px 0',
        display: 'flex',
        flexDirection: 'column'
      }}>
        {tools.map((tool) => {
          const isActive = currentPath === tool.path;

          return (
            <motion.div
              key={tool.id}
              onClick={() => onNavigate(tool.path)}
              style={{
                height: '40px',
                margin: '2px 8px',
                borderRadius: '6px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: isCollapsed ? 'center' : 'flex-start',
                padding: isCollapsed ? '0' : '0 12px',
                cursor: 'pointer',
                backgroundColor: isActive ? '#e9ecef' : 'transparent',
                color: isActive ? '#495057' : '#6c757d',
                transition: 'all 0.15s ease'
              }}
              whileHover={{
                backgroundColor: isActive ? '#e9ecef' : '#f8f9fa',
                color: '#495057'
              }}
            >
              <span style={{
                fontSize: '16px',
                minWidth: '16px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}>
                {tool.icon}
              </span>

              {!isCollapsed && (
                <motion.span
                  initial={{ opacity: 0, x: -10 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: 0.05 }}
                  style={{
                    marginLeft: '12px',
                    fontSize: '14px',
                    fontWeight: '500'
                  }}
                >
                  {tool.name}
                </motion.span>
              )}
            </motion.div>
          );
        })}
      </nav>

      {/* Toggle Button */}
      <div style={{
        height: '48px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        borderTop: '1px solid #e9ecef'
      }}>
        <motion.button
          onClick={() => setIsCollapsed(!isCollapsed)}
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          style={{
            width: '28px',
            height: '28px',
            border: 'none',
            backgroundColor: 'transparent',
            borderRadius: '4px',
            cursor: 'pointer',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: '#6c757d',
            fontSize: '14px',
            transition: 'all 0.15s ease'
          }}
          onMouseEnter={(e) => {
            e.currentTarget.style.backgroundColor = '#f8f9fa';
            e.currentTarget.style.color = '#495057';
          }}
          onMouseLeave={(e) => {
            e.currentTarget.style.backgroundColor = 'transparent';
            e.currentTarget.style.color = '#6c757d';
          }}
        >
          <motion.span
            animate={{ rotate: isCollapsed ? 0 : 180 }}
            transition={{ duration: 0.2 }}
          >
            ←
          </motion.span>
        </motion.button>
      </div>
    </motion.div>
  );
};