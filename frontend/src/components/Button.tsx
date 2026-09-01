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
// apply against them. bg-{secondary,accent}-{shade,tint}-NN (tailwind.config.css) work around
// that: shade mixes toward black (solid variant's opaque hover/active), tint mixes toward the
// page's own cream background (outline variant's faint, on-theme hover/active wash) - both stay
// fully opaque for the same reason: a translucent mix toward `transparent` reliably collapses to
// fully transparent once nested inside Tailwind's generated @layer/@media/:hover structure
// (confirmed via computed-style testing on this exact setup, even though the identical CSS works
// when applied outside it).
const SOLID_CLASSES: Record<ButtonTone, string> = {
    secondary: 'bg-secondary text-primary hover:bg-secondary-shade-90 active:bg-secondary-shade-80',
    accent: 'bg-accent text-primary hover:bg-accent-shade-90 active:bg-accent-shade-80',
    danger: 'bg-red-100 text-red-700 hover:bg-red-200 active:bg-red-300',
};

const OUTLINE_CLASSES: Record<ButtonTone, string> = {
    secondary: 'bg-transparent border border-border hover:bg-secondary-tint-5 active:bg-secondary-tint-12',
    accent: 'bg-transparent border border-border hover:bg-accent-tint-5 active:bg-accent-tint-12',
    danger: 'bg-transparent border border-red-200 hover:bg-red-100 active:bg-red-200',
};

const GHOST_CLASSES: Record<ButtonTone, string> = {
    secondary: 'bg-transparent hover:bg-secondary active:bg-secondary-shade-90',
    accent: 'bg-transparent hover:bg-accent active:bg-accent-shade-90',
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
