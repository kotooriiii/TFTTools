import { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";

// Type definitions for the props
interface BubbleProps {
    x: number;
    y: number;
    label: string;
    onClick: () => void;
    delay?: number;
}

interface AnimatedLineProps {
    x1: number;
    y1: number;
    x2: number;
    y2: number;
    delay?: number;
}

// Bubble component definition with TypeScript types
const Bubble: React.FC<BubbleProps> = ({ x, y, label, onClick, delay = 0 }) => (
    <motion.div
        className="absolute rounded-full bg-blue-500 text-white text-sm flex items-center justify-center shadow-lg cursor-pointer"
        style={{
            left: x,
            top: y,
            width: 60,
            height: 60,
            transform: "translate(-50%, -50%)",
        }}
        initial={{ scale: 0 }}
        animate={{ scale: 1 }}
        exit={{ scale: 0 }}
        transition={{ type: "spring", stiffness: 200, damping: 12, delay }}
        onClick={onClick}
    >
        {label}
    </motion.div>
);

// AnimatedLine component definition with TypeScript types
const AnimatedLine: React.FC<AnimatedLineProps> = ({ x1, y1, x2, y2, delay = 0 }) => (
    <motion.line
        x1={x1}
        y1={y1}
        x2={x1}
        y2={y1}
        stroke="#ccc"
        strokeWidth="2"
        initial={{ x2: x1, y2: y1 }}
        animate={{ x2, y2 }}
        exit={{ x2: x1, y2: y1 }}
        transition={{ duration: 0.3, delay }}
    />
);

// Main component: BubbleGraph with TypeScript types
const BubbleGraph: React.FC = () => {
    const [expanded, setExpanded] = useState<boolean>(false);
    const [center, setCenter] = useState<{ x: number, y: number }>({ x: 0, y: 0 });

    useEffect(() => {
        // Set center position dynamically
        setCenter({
            x: window.innerWidth / 2,
            y: window.innerHeight / 2
        });
    }, []); // This will run only once when the component mounts

    // Generate child bubbles when expanded
    const children = expanded
        ? Array.from({ length: 6 }).map((_, i) => {
            const angle = Math.PI * (i / 5); // Spread from 0 to π (180 degrees)
            return {
                x: center.x + 150 * Math.cos(angle),
                y: center.y - 150 * Math.sin(angle),
                label: `C${i + 1}`,
                delay: 0.2 + i * 0.1,
            };
        })
        : [];

    return (
        <div className="relative w-full h-screen bg-white">
            <svg className="absolute top-0 left-0 w-full h-full pointer-events-none">
                <AnimatePresence>
                    {children.map((child, i) => (
                        <AnimatedLine
                            key={i}
                            x1={center.x}
                            y1={center.y}
                            x2={child.x}
                            y2={child.y}
                            delay={child.delay - 0.15}
                        />
                    ))}
                </AnimatePresence>
            </svg>

            <Bubble
                x={center.x}
                y={center.y}
                label="Root"
                onClick={() => setExpanded(!expanded)}
            />

            <AnimatePresence>
                {children.map((child, i) => (
                    <Bubble
                        key={i}
                        x={child.x}
                        y={child.y}
                        label={child.label}
                        delay={child.delay}
                    />
                ))}
            </AnimatePresence>
        </div>
    );
};

// Export the main BubbleGraph component
export default BubbleGraph;
