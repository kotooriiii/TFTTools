import React from 'react';

type ButtonVariant = 'solid' | 'outline' | 'ghost';
type ButtonTone = 'secondary' | 'accent' | 'danger';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: ButtonVariant;
    tone?: ButtonTone;
    /** Forces the solid resting look regardless of `variant` - for toggle/selected buttons (e.g. a theme picker). */
    selected?: boolean;
}

const RESTING_CLASSES: Record<ButtonVariant, Record<ButtonTone, string>> = {
    solid: {
        secondary: 'bg-secondary text-primary',
        accent: 'bg-accent text-primary',
        danger: 'bg-red-100 text-red-700',
    },
    outline: {
        secondary: 'bg-transparent border border-border',
        accent: 'bg-transparent border border-border',
        danger: 'bg-transparent border border-red-200',
    },
    ghost: {
        secondary: 'bg-transparent',
        accent: 'bg-transparent',
        danger: 'bg-transparent',
    },
};

// Every button shades toward the app's accent color on hover/press, regardless of its own resting
// variant - one consistent interaction cue app-wide - except danger, which stays red so a delete
// button doesn't lose its warning cue. bg-accent-tint-85 (tailwind.config.css) mixes 85% accent
// with white for hover; active goes further, to a pure/undiluted accent (100%, via the existing
// bg-accent utility) - kept as an explicit blend rather than hardcoded so the percentage is easy
// to dial down later.
const HOVER_ACTIVE_CLASSES: Record<ButtonTone, string> = {
    secondary: 'hover:bg-accent-tint-85 active:bg-accent',
    accent: 'hover:bg-accent-tint-85 active:bg-accent',
    danger: 'hover:bg-red-200 active:bg-red-300',
};

/**
 * Shared button - owns background/border color (resting per variant+tone, hover/active shared
 * across all of them) and the press-shrink scale effect. Padding, sizing, radius, font, and gap
 * stay in the caller's className so this never collides with per-site layout classes.
 */
export const Button: React.FC<ButtonProps> = ({
    variant = 'solid',
    tone = 'secondary',
    selected = false,
    type = 'button',
    className = '',
    children,
    ...rest
}) => {
    const effectiveVariant = selected ? 'solid' : variant;
    const restingClasses = RESTING_CLASSES[effectiveVariant][tone];
    const hoverActiveClasses = HOVER_ACTIVE_CLASSES[tone];
    const scaleClasses = effectiveVariant === 'ghost' ? '' : 'active:scale-[0.97]';

    return (
        <button
            type={type}
            className={`cursor-pointer transition-all duration-150 disabled:opacity-50 disabled:cursor-not-allowed ${restingClasses} ${hoverActiveClasses} ${scaleClasses} ${className}`}
            {...rest}
        >
            {children}
        </button>
    );
};
