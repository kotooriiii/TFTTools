package com.tfttools.comps.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * A user's saved composition: identity + hex-board position per unit only (see
 * {@link Placement}). No name/label and no frozen team code - both are either unnecessary
 * (content + save date identifies a comp) or computed on demand so they can't go stale.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comp
{
    private UUID id;

    private UUID userId;

    private List<Placement> placements;

    private Instant createdAt;
}
