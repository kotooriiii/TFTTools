import React from 'react';
import { motion } from 'framer-motion';
import { Button } from './Button';

interface ZoomControlsProps {
    onZoomIn: () => void;
    onZoomOut: () => void;
    onResetZoom: () => void;
    zoom: number;
}

const ICON_BUTTON_CLASSES = 'w-12 h-12 rounded-lg flex items-center justify-center text-xl font-bold shadow-md backdrop-blur-sm';

// outline + accent, same pairing LoginPage's "Continue with Google" button uses - one recognizable
// combination reused instead of a one-off choice for this component.

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
            <Button
                variant="outline"
                tone="accent"
                onClick={onZoomIn}
                title="Zoom In"
                className={ICON_BUTTON_CLASSES}
            >
                +
            </Button>

            <Button
                variant="outline"
                tone="accent"
                onClick={onZoomOut}
                title="Zoom Out"
                className={ICON_BUTTON_CLASSES}
            >
                −
            </Button>

            <motion.div
                style={{
                    width: '48px',
                    height: '32px',
                    backgroundColor: 'var(--color-bg-primary)',
                    border: '2px solid var(--color-border)',
                    borderRadius: '6px',
                    fontSize: '10px',
                    fontWeight: 'bold',
                    color: 'var(--color-text-primary)',
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

            <Button
                variant="outline"
                tone="accent"
                onClick={onResetZoom}
                title="Reset Zoom & Pan"
                className={ICON_BUTTON_CLASSES}
            >
                ⌂
            </Button>
        </div>
    );
};
