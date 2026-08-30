package com.tfttools.comps.service;

import com.tfttools.comps.domain.Comp;
import com.tfttools.comps.dto.CompResponse;
import com.tfttools.comps.dto.PlacementDTO;
import com.tfttools.comps.dto.SaveCompRequest;
import com.tfttools.comps.exception.CompLimitExceededException;
import com.tfttools.comps.exception.CompNotFoundException;
import com.tfttools.comps.repository.CompRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompService
{
    /**
     * Ticket-specified cap: saving past this many comps for a single user is rejected (409).
     */
    private static final int MAX_COMPS_PER_USER = 25;

    private final CompRepository compRepository;

    public CompService(CompRepository compRepository)
    {
        this.compRepository = compRepository;
    }

    public CompResponse save(UUID userId, SaveCompRequest request)
    {
        if (compRepository.countByUserId(userId) >= MAX_COMPS_PER_USER)
        {
            throw new CompLimitExceededException(
                    "You've reached the limit of " + MAX_COMPS_PER_USER + " saved comps. Delete one before saving another.");
        }

        Comp comp = new Comp();
        comp.setUserId(userId);
        comp.setPlacements(request.placements().stream().map(PlacementDTO::toPlacement).toList());

        return CompResponse.from(compRepository.save(comp));
    }

    public List<CompResponse> list(UUID userId)
    {
        return compRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(CompResponse::from)
                .toList();
    }

    public void delete(UUID userId, UUID compId)
    {
        boolean deleted = compRepository.deleteByIdAndUserId(compId, userId);
        if (!deleted)
        {
            throw new CompNotFoundException("Comp not found");
        }
    }
}
