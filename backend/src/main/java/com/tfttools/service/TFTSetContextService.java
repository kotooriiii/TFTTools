
package com.tfttools.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

import com.tfttools.service.CommunityDragonDataService;

import java.util.Comparator;
import java.util.Map;

@Service
public class TFTSetContextService
{

    private final CommunityDragonDataService communityDragonDataService;

    @Getter
    private String currentSetNumber;

    public TFTSetContextService(CommunityDragonDataService communityDragonDataService)
    {
        this.communityDragonDataService = communityDragonDataService;
    }

    /**
     * Resolves the current set number from the same champion roster document that
     * UnitRepository/TraitRepository/EmblemRepository read from (rather than the
     * separately-fetched, fallback-less team planner data), so the resolved set number
     * is guaranteed to have non-empty champion data in that same document.
     */
    @PostConstruct
    public void initialize()
    {
        try
        {
            currentSetNumber = communityDragonDataService.getCommunityDragonData().getSets().entrySet().stream()
                    .filter(entry -> entry.getKey().matches("\\d+"))
                    .filter(entry -> entry.getValue().getChampions() != null && !entry.getValue().getChampions().isEmpty())
                    .map(Map.Entry::getKey)
                    .max(Comparator.comparingInt(Integer::parseInt))
                    .orElse(null);

        } catch (Exception e) {
            System.err.println("Failed to initialize TFT set context at startup: " + e.getMessage());
        }
    }

    public String getCurrentSetKey() {
        return currentSetNumber != null ? "TFTSet" + currentSetNumber : null;
    }
}
