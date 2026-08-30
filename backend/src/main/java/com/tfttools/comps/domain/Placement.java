package com.tfttools.comps.domain;

/**
 * A single unit's identity + hex-board position within a saved {@link Comp}. Identity is by
 * {@code unitApiName} (not display name) so a saved comp survives a display-name rename -
 * everything else about the unit (cost, traits, icon) is rehydrated live from the unit
 * repository at render time, never stored here.
 */
public record Placement(String unitApiName, int row, int col) {
}
