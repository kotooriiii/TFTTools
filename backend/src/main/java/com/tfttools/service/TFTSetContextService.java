
package com.tfttools.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.tfttools.service.CommunityDragonDataService;

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

    @PostConstruct
    public void initialize()
    {
        try
        {
            currentSetNumber = communityDragonDataService.getTeamPlannerData().getSets().keySet().stream().max(String::compareTo).orElse("").substring("TFTSet".length());


        } catch (Exception e) {
            System.err.println("Failed to initialize TFT set context at startup: " + e.getMessage());
        }
    }
    
    public String getCurrentSetKey() {
        return currentSetNumber != null ? "TFTSet" + currentSetNumber : null;
    }
}
