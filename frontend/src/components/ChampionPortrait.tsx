import React, { useState } from 'react';
import { getInitials } from './CompBuilder/hexUtils';

interface ChampionPortraitProps {
    displayName: string;
    iconUrl?: string;
    size: number;
    borderColor?: string;
    style?: React.CSSProperties;
    className?: string;
}

export const ChampionPortrait: React.FC<ChampionPortraitProps> = ({
                                                                        displayName,
                                                                        iconUrl,
                                                                        size,
                                                                        borderColor,
                                                                        style,
                                                                        className
                                                                    }) => {
    const [failed, setFailed] = useState(false);

    const commonStyle: React.CSSProperties = {
        width: size,
        height: size,
        borderRadius: '50%',
        border: borderColor ? `2px solid ${borderColor}` : undefined,
        ...style
    };

    if (!iconUrl || failed) {
        return (
            <div
                className={className}
                style={{
                    ...commonStyle,
                    backgroundColor: '#3B3B1A',
                    color: 'white',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontSize: size * 0.32,
                    fontWeight: 700
                }}
            >
                {getInitials(displayName)}
            </div>
        );
    }

    return (
        <img
            src={iconUrl}
            alt={displayName}
            onError={() => setFailed(true)}
            className={className}
            style={{ ...commonStyle, objectFit: 'cover' }}
        />
    );
};
