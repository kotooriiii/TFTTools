import React from 'react';
import { Button } from './Button';

interface RemoveChipButtonProps {
    onClick: () => void;
    label: string;
    className?: string;
}

/**
 * The small "x" that removes a selected item from a search chip - shared by every chip-style
 * selected-item list (GenericSearchBox, UnitPopupSearchPanel) so shape, centering, and hover color
 * live in one place instead of being reimplemented (and drifting) per call site. Two things that
 * went wrong before this existed, both fixed structurally rather than per instance:
 *  - an "x" character's glyph box isn't perfectly centered in most fonts, and shifts differently
 *    at different sizes - an SVG cross sidesteps that entirely instead of nudging padding by eye.
 *  - a shrink-to-fit box with a plain `rounded` corner renders as an oval the moment its width and
 *    height differ even slightly; a fixed w-5 h-5 square is what actually makes rounded-full a
 *    circle.
 * Tone is always danger regardless of the chip's own color (amber/purple/secondary/accent, ...) -
 * removing something reads the same way everywhere, rather than each chip inventing its own
 * hover treatment to (not) match its own hue.
 */
export const RemoveChipButton: React.FC<RemoveChipButtonProps> = ({onClick, label, className = ''}) => (
    <Button
        variant="ghost"
        tone="danger"
        onClick={onClick}
        aria-label={label}
        className={`w-3 h-3 shrink-0 p-0 rounded-full flex items-center justify-center ${className}`}
    >
        <svg viewBox="0 0 20 20" className="w-2.5 h-2.5" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" aria-hidden="true">
            <path d="M5 5L15 15M15 5L5 15"/>
        </svg>
    </Button>
);
