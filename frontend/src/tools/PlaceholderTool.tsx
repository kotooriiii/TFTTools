import React from 'react';

interface PlaceholderToolProps {
  name: string;
  icon: string;
  description: string;
}

const PlaceholderTool: React.FC<PlaceholderToolProps> = ({ name, icon, description }) => {
  return (
    <div className="flex-1 flex items-center justify-center bg-gray-50">
      <div className="text-center text-gray-500 max-w-md">
        <div className="text-8xl mb-6">{icon}</div>
        <h2 className="text-3xl font-semibold mb-4">{name}</h2>
        <p className="text-lg mb-6">{description}</p>
        <div className="bg-yellow-100 border border-yellow-300 rounded-lg p-4">
          <p className="text-yellow-800">
            This tool is coming soon! The framework is ready for implementation.
          </p>
        </div>
      </div>
    </div>
  );
};

export default PlaceholderTool;