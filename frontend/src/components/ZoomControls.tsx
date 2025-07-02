import React from 'react';
import { motion } from 'framer-motion';

interface ZoomControlsProps {
    onZoomIn: () => void;
    onZoomOut: () => void;
    onResetZoom: () => void;
    zoom: number;
}

export const ZoomControls: React.FC<ZoomControlsProps> = ({
                                                              onZoomIn,
                                                              onZoomOut,
                                                              onResetZoom,
                                                              zoom
                                                          }) => {
    return (
        <div style={{
            position: 'absolute',
            top: '20px',
            right: '20px',
            display: 'flex',
            flexDirection: 'column',
            gap: '8px',
            zIndex: 1000
        }}>
            <motion.button
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                onClick={onZoomIn}
                style={{
                    width: '48px',
                    height: '48px',
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    border: '2px solid #594b42',
                    borderRadius: '8px',
                    fontSize: '20px',
                    fontWeight: 'bold',
                    color: '#594b42',
                    cursor: 'pointer',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    boxShadow: '0 2px 8px rgba(0, 0, 0, 0.15)',
                    backdropFilter: 'blur(10px)'
                }}
                title="Zoom In"
            >
                +
            </motion.button>

            <motion.button
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                onClick={onZoomOut}
                style={{
                    width: '48px',
                    height: '48px',
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    border: '2px solid #594b42',
                    borderRadius: '8px',
                    fontSize: '20px',
                    fontWeight: 'bold',
                    color: '#594b42',
                    cursor: 'pointer',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    boxShadow: '0 2px 8px rgba(0, 0, 0, 0.15)',
                    backdropFilter: 'blur(10px)'
                }}
                title="Zoom Out"
            >
                −
            </motion.button>

            <motion.div
                style={{
                    width: '48px',
                    height: '32px',
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    border: '2px solid #594b42',
                    borderRadius: '6px',
                    fontSize: '10px',
                    fontWeight: 'bold',
                    color: '#594b42',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    boxShadow: '0 2px 8px rgba(0, 0, 0, 0.15)',
                    backdropFilter: 'blur(10px)'
                }}
                title="Current Zoom Level"
            >
                {Math.round(zoom * 100)}%
            </motion.div>

            <motion.button
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                onClick={onResetZoom}
                style={{
                    width: '48px',
                    height: '48px',
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    border: '2px solid #594b42',
                    borderRadius: '8px',
                    fontSize: '12px',
                    fontWeight: 'bold',
                    color: '#594b42',
                    cursor: 'pointer',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    boxShadow: '0 2px 8px rgba(0, 0, 0, 0.15)',
                    backdropFilter: 'blur(10px)'
                }}
                title="Reset Zoom & Pan"
            >
                ⌂
            </motion.button>
        </div>
    );
};