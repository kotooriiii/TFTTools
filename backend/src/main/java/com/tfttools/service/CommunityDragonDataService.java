package com.tfttools.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.tfttools.domain.communitydragon.TeamPlannerData;
import com.tfttools.web.client.CommunityDragonWebClient;
import com.tfttools.domain.communitydragon.CommunityDragonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

@Service
public class CommunityDragonDataService {

    private static final Logger logger = LoggerFactory.getLogger(CommunityDragonDataService.class);
    private static final String FALLBACK_RESOURCE_PATH = "/com/tfttools/domain/repository/communitydragon/en_us.json";

    @Value("${tft.communitydragon.cache.duration.hours:1}")
    private int cacheDurationHours;

    @Value("${tft.communitydragon.fallback.enabled:true}")
    private boolean fallbackEnabled;

    private final CommunityDragonWebClient webClient;

    private CommunityDragonObject cachedData;
    private TeamPlannerData cachedTeamPlannerData;

    private Instant lastFetchTime;
    private String lastModified;

    @Autowired
    public CommunityDragonDataService(CommunityDragonWebClient webClient) {
        this.webClient = webClient;
    }

    public CommunityDragonObject getCommunityDragonData() {
        if (shouldRefreshCache()) {
            refreshCache();
        }
        return cachedData;
    }

    private boolean shouldRefreshCache() {
        if (cachedData == null || lastFetchTime == null) {
            return true;
        }

        Duration timeSinceLastFetch = Duration.between(lastFetchTime, Instant.now());
        return timeSinceLastFetch.toHours() >= cacheDurationHours;
    }

    private void refreshCache() {
        try {
            logger.info("Refreshing Community Dragon data cache");

            CommunityDragonObject freshData = fetchFromWebClient();

            if (freshData != null) {
                cachedData = freshData;
                lastFetchTime = Instant.now();
                logger.info("Successfully refreshed Community Dragon data cache");
            } else if (fallbackEnabled && cachedData == null) {
                logger.warn("Failed to fetch from web client, loading from local fallback");
                cachedData = loadFromLocalResource();
                lastFetchTime = Instant.now();
            }

        } catch (Exception e) {
            logger.error("Error refreshing cache", e);

            if (fallbackEnabled && cachedData == null) {
                try {
                    logger.info("Loading Community Dragon data from local fallback");
                    cachedData = loadFromLocalResource();
                    lastFetchTime = Instant.now();
                } catch (IOException ioException) {
                    logger.error("Failed to load from local fallback", ioException);
                    throw new RuntimeException("Failed to load Community Dragon data", ioException);
                }
            } else if (cachedData == null) {
                throw new RuntimeException("Failed to load Community Dragon data and no fallback available", e);
            }
        }
    }

    private CommunityDragonObject fetchFromWebClient() {
        try {
            logger.debug("Fetching Community Dragon data using web client");

            Mono<CommunityDragonObject> responseMono;

            // Use conditional fetch if we have lastModified header
            if (lastModified != null && !lastModified.isEmpty()) {
                responseMono = webClient.fetchDataIfModified(lastModified);
            } else {
                responseMono = webClient.fetchData();
            }

            CommunityDragonObject result = responseMono
                    .onErrorResume(ex -> {
                        logger.error("Error fetching from web client: {}\n\n{}", ex.getMessage(), ex.getStackTrace());
                        return Mono.empty();
                    })
                    .block(); // Convert to blocking for existing API compatibility

            if (result != null) {
                logger.debug("Successfully fetched Community Dragon data");
                return result;
            }

            // If result is null (304 Not Modified or error), return existing cache
            return cachedData;

        } catch (Exception e) {
            logger.error("Failed to fetch Community Dragon data from web client", e);
            return null;
        }
    }

    private CommunityDragonObject loadFromLocalResource() throws IOException {
        logger.debug("Loading Community Dragon data from local resource: {}", FALLBACK_RESOURCE_PATH);

        ClassPathResource resource = new ClassPathResource(FALLBACK_RESOURCE_PATH);
        try (InputStream inputStream = resource.getInputStream()) {
            ObjectMapper mapper = CommunityDragonWebClient.createCommunityDragonObjectMapper();
            return mapper.readValue(inputStream, CommunityDragonObject.class);
        }
    }

    /**
     * Forces a refresh of the cached data on the next call to getCommunityDragonData()
     */
    public void invalidateCache() {
        logger.info("Cache invalidated, will refresh on next request");
        cachedData = null;
        lastFetchTime = null;
        lastModified = null;
    }

    public TeamPlannerData getTeamPlannerData() {
        if (cachedTeamPlannerData == null) {
            cachedTeamPlannerData = fetchTeamPlannerData();
        }
        return cachedTeamPlannerData;
    }

    private TeamPlannerData fetchTeamPlannerData() {
        try {
            logger.debug("Fetching Team Planner data using web client");

            TeamPlannerData result = webClient.fetchTeamPlannerData()
                    .timeout(Duration.ofSeconds(30))
                    .onErrorResume(ex -> {
                        logger.error("Error fetching team planner data from web client: {}", ex.getMessage(), ex);
                        return Mono.empty();
                    })
                    .block();

            if (result != null) {
                logger.debug("Successfully fetched Team Planner data");
                return result;
            }

            logger.warn("Failed to fetch team planner data from web client, no fallback available");
            return null;

        } catch (Exception e) {
            logger.error("Failed to fetch Team Planner data", e);
            return null;
        }
    }


    /**
     * Returns information about the current cache status
     */
    public CacheStatus getCacheStatus() {
        boolean needsRefresh = shouldRefreshCache();
        return new CacheStatus(cachedData != null, lastFetchTime, lastModified, needsRefresh);
    }

    public static class CacheStatus {
        private final boolean hasCachedData;
        private final Instant lastFetchTime;
        private final String lastModified;
        private final boolean needsRefresh;

        public CacheStatus(boolean hasCachedData, Instant lastFetchTime, String lastModified, boolean needsRefresh) {
            this.hasCachedData = hasCachedData;
            this.lastFetchTime = lastFetchTime;
            this.lastModified = lastModified;
            this.needsRefresh = needsRefresh;
        }

        public boolean hasCachedData() { return hasCachedData; }
        public Instant getLastFetchTime() { return lastFetchTime; }
        public String getLastModified() { return lastModified; }
        public boolean needsRefresh() { return needsRefresh; }
    }
}