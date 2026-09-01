import React from 'react';

type ButtonVariant = 'solid' | 'outline' | 'ghost';
type ButtonTone = 'secondary' | 'accent' | 'danger';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: ButtonVariant;
    tone?: ButtonTone;
    /** Forces the solid resting/hover/active look regardless of `variant` - for toggle/selected buttons (e.g. a theme picker). */
    selected?: boolean;
}

const SOLID_CLASSES: Record<ButtonTone, string> = {
    secondary: 'bg-secondary text-primary hover:bg-secondary/90 active:bg-secondary/80',
    accent: 'bg-accent text-primary hover:bg-accent/90 active:bg-accent/80',
    danger: 'bg-red-100 text-red-700 hover:bg-red-200 active:bg-red-300',
};

const OUTLINE_CLASSES: Record<ButtonTone, string> = {
    secondary: 'bg-transparent border border-border text-primary hover:bg-secondary/10 active:bg-secondary/20',
    accent: 'bg-transparent border border-border text-primary hover:bg-accent/10 active:bg-accent/20',
    danger: 'bg-transparent border border-red-200 text-red-700 hover:bg-red-100 active:bg-red-200',
};

const GHOST_CLASSES: Record<ButtonTone, string> = {
    secondary: 'bg-transparent hover:bg-secondary active:bg-secondary/70',
    accent: 'bg-transparent hover:bg-accent active:bg-accent/70',
    danger: 'bg-transparent hover:bg-red-100 active:bg-red-200',
};

/**
 * Shared button - owns only background color (resting/hover/active), border color (outline),
 * and the press-scale effect. Padding, sizing, radius, font, and gap stay in the caller's
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
    const scaleClasses = effectiveVariant === 'ghost' ? '' : 'active:scale-[0.97]';

    return (
        <button
            type={type}
            className={`cursor-pointer transition-all duration-150 disabled:opacity-50 disabled:cursor-not-allowed ${variantClasses} ${scaleClasses} ${className}`}
            {...rest}
        >
            {children}
        </button>
    );
};
