package com.tfttools.service;

import com.tfttools.repository.EmblemRepository;
import com.tfttools.repository.TraitRepository;
import com.tfttools.repository.UnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataRefreshService {
    
    private static final Logger logger = LoggerFactory.getLogger(DataRefreshService.class);
    
    private final CommunityDragonDataService dataService;
    private final TeamPlannerService teamPlannerService;

    private final TraitRepository traitRepository;
    private final UnitRepository unitRepository;
    private final EmblemRepository emblemRepository;

    @Autowired
    public DataRefreshService(CommunityDragonDataService dataService,
                             TraitRepository traitRepository,
                             UnitRepository unitRepository,
                             EmblemRepository emblemRepository,
                             TeamPlannerService teamPlannerService) {
        this.dataService = dataService;
        this.traitRepository = traitRepository;
        this.unitRepository = unitRepository;
        this.emblemRepository = emblemRepository;
        this.teamPlannerService = teamPlannerService;
    }
    
    public void refreshAllData() {
        try {
            logger.info("Starting full data refresh");
            
            // 1. Invalidate the cache first
            dataService.invalidateCache();
            
            // 2. Force fetch fresh data
            dataService.getCommunityDragonData();
            dataService.getTeamPlannerData();
            
            // 3. Reload all repositories in the correct order
            // Traits first (no dependencies)
            traitRepository.reloadTraits();
            logger.debug("Reloaded traits");
            
            // Units next (depends on traits)
            unitRepository.reloadUnits();
            logger.debug("Reloaded units");
            
            // Emblems last (depends on traits)
            emblemRepository.reloadEmblems();
            logger.debug("Reloaded emblems");
            
            // Team planner codes
            teamPlannerService.refreshTeamPlannerCodes();
            logger.debug("Reloaded team planner codes");
            
            logger.info("Successfully completed full data refresh");
            
        } catch (Exception e) {
            logger.error("Failed to refresh data", e);
            throw new RuntimeException("Data refresh failed", e);
        }
    }
}
