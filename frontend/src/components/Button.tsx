import React from 'react';

type ButtonVariant = 'solid' | 'outline' | 'ghost';
type ButtonTone = 'secondary' | 'accent' | 'danger';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: ButtonVariant;
    tone?: ButtonTone;
    /** Forces the solid resting/hover/active look regardless of `variant` - for toggle/selected buttons (e.g. a theme picker). */
    selected?: boolean;
}

// `bg-secondary`/`bg-accent` are hand-rolled `@utility` blocks (tailwind.config.css), not part of
// Tailwind's auto-generated color system, so the `/NN` opacity-modifier syntax silently fails to
// apply against them. bg-{secondary,accent}-shade-NN (tailwind.config.css) work around that for
// the solid variant's opaque hover/active shade, mixed toward black. (A translucent background
// tint was tried for the outline variant first, but it read as a flat, characterless grey/wash
// regardless of the color mixed in - a grow + tone-colored border reads as feedback without
// needing a background color at all.)
const SOLID_CLASSES: Record<ButtonTone, string> = {
    secondary: 'bg-secondary text-primary hover:bg-secondary-shade-90 active:bg-secondary-shade-80 active:scale-[0.97]',
    accent: 'bg-accent text-primary hover:bg-accent-shade-90 active:bg-accent-shade-80 active:scale-[0.97]',
    danger: 'bg-red-100 text-red-700 hover:bg-red-200 active:bg-red-300 active:scale-[0.97]',
};

const OUTLINE_CLASSES: Record<ButtonTone, string> = {
    secondary: 'bg-transparent border border-border hover:border-secondary hover:scale-105 active:border-secondary active:scale-[0.97]',
    accent: 'bg-transparent border border-border hover:border-accent hover:scale-105 active:border-accent active:scale-[0.97]',
    danger: 'bg-transparent border border-red-200 hover:bg-red-100 active:bg-red-200 active:scale-[0.97]',
};

const GHOST_CLASSES: Record<ButtonTone, string> = {
    secondary: 'bg-transparent hover:bg-secondary active:bg-secondary-shade-90',
    accent: 'bg-transparent hover:bg-accent active:bg-accent-shade-90',
    danger: 'bg-transparent hover:bg-red-100 active:bg-red-200',
};

/**
 * Shared button - owns background/border color (resting/hover/active) and the hover-grow /
 * press-shrink scale effect. Padding, sizing, radius, font, and gap stay in the caller's
 * className so this never collides with per-site layout classes.
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
    const variantClasses =
        effectiveVariant === 'solid' ? SOLID_CLASSES[tone] :
        effectiveVariant === 'outline' ? OUTLINE_CLASSES[tone] :
        GHOST_CLASSES[tone];

    return (
        <button
            type={type}
            className={`cursor-pointer transition-all duration-150 disabled:opacity-50 disabled:cursor-not-allowed ${variantClasses} ${className}`}
            {...rest}
        >
            {children}
        </button>
    );
};
