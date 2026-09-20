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

// Solid lightens toward white on hover (bg-{tone}-shade-200, tailwind.config.css) - the opposite
// of darkening, so hovering reads as the button responding instead of going dull. Active reuses
// the exact same color as hover: press feedback comes from the scale-down below, not from a
// second, deeper shade. The hover/active ring itself is the shared `interactive-ring` utility
// applied once in the base className below, not repeated per tone here.
const SOLID_HOVER_ACTIVE: Record<ButtonTone, string> = {
    secondary: 'hover:bg-secondary-shade-200 active:bg-secondary-shade-200',
    accent: 'hover:bg-accent-shade-200 active:bg-accent-shade-200',
    danger: 'hover:bg-red-200 active:bg-red-200',
};

// Ghost rests fully transparent, so a lightened tint (solid's own hover color) would barely
// register against it - instead it jumps straight to solid's RESTING color on both hover and
// active: transparent -> tone-100, mirroring how danger already goes transparent -> red-100.
const GHOST_HOVER_ACTIVE: Record<ButtonTone, string> = {
    secondary: 'hover:bg-secondary active:bg-secondary',
    accent: 'hover:bg-accent active:bg-accent',
    danger: 'hover:bg-red-100 active:bg-red-100',
};

// Outline keeps its idle border at rest, but on hover/active the border goes fully transparent
// at the exact moment the shared ring (see `interactive-ring` below) appears alongside a gooey
// blob fill (BLOB_LAYER below) - the border and the ring are the same boundary line handing off,
// never both drawn at once.
const OUTLINE_HOVER_ACTIVE: Record<ButtonTone, string> = {
    secondary: 'hover:border-transparent active:border-transparent',
    accent: 'hover:border-transparent active:border-transparent',
    danger: 'hover:border-transparent active:border-transparent',
};

const HOVER_ACTIVE_CLASSES: Record<ButtonVariant, Record<ButtonTone, string>> = {
    solid: SOLID_HOVER_ACTIVE,
    outline: OUTLINE_HOVER_ACTIVE,
    ghost: GHOST_HOVER_ACTIVE,
};

// The blob's ink color, per tone - same color solid rests at, so outline's hover fill and
// solid's resting fill read as the same material.
const BLOB_INK_CLASSES: Record<ButtonTone, string> = {
    secondary: '[--blob-ink:var(--color-bg-secondary)]',
    accent: '[--blob-ink:var(--color-bg-accent)]',
    danger: '[--blob-ink:var(--color-red-200)]',
};

// interactive-ring (tailwind.config.css) defaults its ring color to the same --color-border every
// input field's own focus ring uses; danger just swaps the ring's color via --ring-tone, the same
// technique BLOB_INK_CLASSES uses for --blob-ink, instead of a separate ring rule per tone.
const RING_TONE_CLASSES: Record<ButtonTone, string> = {
    secondary: '',
    accent: '',
    danger: '[--ring-tone:var(--color-red-300)]',
};

const BLOB_COUNT = 4;

/**
 * Outline-only: an SVG-goo-filtered fill that rises from below and merges into a solid color on
 * hover/active, referencing the shared <filter id="btn-goo-filter"> defined once in App.tsx.
 * -inset-px (not inset-0) so the clipping layer reaches the button's true outer edge - the same
 * width as the border it's replacing - instead of stopping at the padding edge and leaving a
 * sliver of the (now-transparent) border unfilled.
 */
const OutlineBlobLayer: React.FC = () => (
    <span className="absolute -inset-px -z-10 overflow-hidden rounded-[inherit]">
        <span className="relative block h-full [filter:url('#btn-goo-filter')]">
            {Array.from({length: BLOB_COUNT}, (_, i) => (
                <span
                    key={i}
                    className="absolute top-0 h-full w-[26%] scale-[1.6] translate-y-[160%] rounded-full bg-[var(--blob-ink)] transition-transform duration-[450ms] group-hover:translate-y-0 group-active:translate-y-0"
                    style={{left: `${i * 24}%`, transitionDelay: `${i * 60}ms`}}
                />
            ))}
        </span>
    </span>
);

/**
 * Shared button - owns background/border color (resting per variant+tone, hover/active lightening
 * through the same hue, outline additionally filling via a gooey blob) and the press-shrink/grow
 * scale effect. Padding, sizing, radius, font, and gap stay in the caller's className so this
 * never collides with per-site layout classes.
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
    const isOutline = effectiveVariant === 'outline';
    const restingClasses = RESTING_CLASSES[effectiveVariant][tone];
    const hoverActiveClasses = HOVER_ACTIVE_CLASSES[effectiveVariant][tone];
    const blobInkClasses = isOutline ? BLOB_INK_CLASSES[tone] : '';
    const ringToneClasses = RING_TONE_CLASSES[tone];

    return (
        <button
            type={type}
            className={`group relative z-0 cursor-pointer transition-all duration-150 hover:scale-[1.02] active:scale-[0.97] interactive-ring disabled:opacity-50 disabled:cursor-not-allowed ${restingClasses} ${hoverActiveClasses} ${blobInkClasses} ${ringToneClasses} ${className}`}
            {...rest}
        >
            {isOutline && <OutlineBlobLayer/>}
            {children}
        </button>
    );
};
