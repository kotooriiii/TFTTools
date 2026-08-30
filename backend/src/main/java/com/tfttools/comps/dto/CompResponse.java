package com.tfttools.comps.dto;

import com.tfttools.comps.domain.Comp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CompResponse(
        UUID id,
        List<PlacementDTO> placements,
        Instant createdAt
) {
    public static CompResponse from(Comp comp)
    {
        return new CompResponse(
                comp.getId(),
                comp.getPlacements().stream().map(PlacementDTO::from).toList(),
                comp.getCreatedAt()
        );
    }
}
