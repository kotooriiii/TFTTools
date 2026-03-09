import React from 'react';

const PlaceholderTool: React.FC = () => {
  return (
    <div className="flex-1 flex items-center justify-center">
      <div className="text-center text-gray-500 max-w-md">
        <div className="text-8xl mb-6">{ '🛠' }</div>
        <h2 className="text-3xl font-semibold mb-4">{'Under Construction'}</h2>
        <p className="text-lg mb-6">{'TBA'}</p>
        <div className="bg-accent border border-border rounded-lg p-4">
          <p className="text-secondary">
            This tool is coming soon! The framework is ready for implementation.
          </p>
        </div>
      </div>
    </div>
  );
};

export default PlaceholderTool;