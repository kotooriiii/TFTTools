package com.tfttools.controller;

import com.tfttools.service.CommunityDragonDataService;
import com.tfttools.service.DataRefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cdragon")
public class CommunityDragonController {

    private final CommunityDragonDataService dataService;
    private final DataRefreshService refreshService; // NEW

    @Autowired
    public CommunityDragonController(CommunityDragonDataService dataService,
                                     DataRefreshService refreshService) {
        this.dataService = dataService;
        this.refreshService = refreshService;
    }

    @GetMapping("/cache/status")
    public ResponseEntity<CommunityDragonDataService.CacheStatus> getCacheStatus() {
        return ResponseEntity.ok(dataService.getCacheStatus());
    }

    @PostMapping("/cache/refresh")
    public ResponseEntity<String> refreshCache() {
        try {
            // Use the new refresh service instead of just invalidating cache
            refreshService.refreshAllData();
            return ResponseEntity.ok("Cache and all data refreshed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to refresh cache: " + e.getMessage());
        }
    }
}
