import React from 'react';

type TextInputProps = React.InputHTMLAttributes<HTMLInputElement>;

/**
 * Shared text input - owns border/ring color (resting border-border; on focus the border goes
 * transparent at the exact moment the same 2px ring Button.tsx uses (interactive-ring's twin,
 * field-ring, tailwind.config.css) appears, so the two never stack into an unpredictable combined
 * thickness) plus the base bg/text color. Padding, sizing, rounding, and font stay in the
 * caller's className, same convention as Button.tsx.
 */
export const TextInput: React.FC<TextInputProps> = ({className = '', ...rest}) => (
    <input
        className={`border border-border focus:border-transparent bg-primary text-primary field-ring ${className}`}
        {...rest}
    />
);
