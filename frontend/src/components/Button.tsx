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

// Every tone gets the same "three shades of one hue" feel danger already had (bg-red-100 ->
// hover:bg-red-200 -> active:bg-red-300): the tone's own base color plays the role of "100", and
// bg-{secondary,accent}-200/-300 (tailwind.config.css) step it progressively darker. Solid rests
// at its own base color and steps through -200/-300 on hover/active; outline/ghost rest transparent
// and step through the SAME base-color/-200 pair, mirroring how outline/ghost danger go
// transparent -> red-100 -> red-200 (one tier lower than solid's hover/active).
const SOLID_HOVER_ACTIVE: Record<ButtonTone, string> = {
    secondary: 'hover:bg-secondary-200 active:bg-secondary-300',
    accent: 'hover:bg-accent-200 active:bg-accent-300',
    danger: 'hover:bg-red-200 active:bg-red-300',
};

const TRANSPARENT_HOVER_ACTIVE: Record<ButtonTone, string> = {
    secondary: 'hover:bg-secondary active:bg-secondary-200',
    accent: 'hover:bg-accent active:bg-accent-200',
    danger: 'hover:bg-red-100 active:bg-red-200',
};

const HOVER_ACTIVE_CLASSES: Record<ButtonVariant, Record<ButtonTone, string>> = {
    solid: SOLID_HOVER_ACTIVE,
    outline: TRANSPARENT_HOVER_ACTIVE,
    ghost: TRANSPARENT_HOVER_ACTIVE,
};

/**
 * Shared button - owns background/border color (resting per variant+tone, hover/active stepping
 * darker through the same hue) and the press-shrink scale effect. Padding, sizing, radius, font,
 * and gap stay in the caller's className so this never collides with per-site layout classes.
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
    const hoverActiveClasses = HOVER_ACTIVE_CLASSES[effectiveVariant][tone];
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
