package com.tfttools.service;

import com.tfttools.domain.Unit;
import com.tfttools.web.client.CommunityDragonWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Caches champion HUD icons fetched from Community Dragon into S3, keyed by TFT set and champion,
 * so the app serves portraits from its own CDN rather than depending on Community Dragon at request time.
 */
@Service
public class ChampionIconCacheService
{
    private static final Logger logger = LoggerFactory.getLogger(ChampionIconCacheService.class);
    private static final String CACHE_CONTROL = "public, max-age=31536000, immutable";

    private final S3Client s3Client;
    private final CommunityDragonWebClient communityDragonWebClient;
    private final TFTSetContextService setContextService;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.cdn.base-url}")
    private String cdnBaseUrl;

    public ChampionIconCacheService(S3Client s3Client, CommunityDragonWebClient communityDragonWebClient, TFTSetContextService setContextService)
    {
        this.s3Client = s3Client;
        this.communityDragonWebClient = communityDragonWebClient;
        this.setContextService = setContextService;
    }

    /**
     * Deterministic CDN URL for a champion's icon. Does not verify the object exists in S3 -
     * callers rely on {@link #ensureAllCached(Set)} having already run.
     */
    public String getIconUrl(String apiName)
    {
        return cdnBaseUrl + "/" + iconKey(apiName);
    }

    /**
     * Ensures every unit's icon is cached in S3, fetching from Community Dragon on a cache miss.
     * Runs on a background thread so callers (app startup, manual data refresh) are not blocked.
     * A single unit's failure is logged and skipped rather than failing the whole batch.
     */
    public void ensureAllCached(Set<Unit> units)
    {
        CompletableFuture.runAsync(() -> units.forEach(unit -> {
            try
            {
                ensureCached(unit);
            } catch (Exception e)
            {
                logger.warn("Failed to cache champion icon for {}", unit.getApiName(), e);
            }
        }));
    }

    private void ensureCached(Unit unit)
    {
        String apiName = unit.getApiName();
        String key = iconKey(apiName);

        if (objectExists(key))
        {
            return;
        }

        String tileIconPath = unit.getTileIconPath();
        if (tileIconPath == null || tileIconPath.isBlank() || tileIconPath.equalsIgnoreCase("None"))
        {
            throw new IllegalStateException("No tileIcon available from Community Dragon for " + apiName);
        }

        // Community Dragon serves raw game assets by lowercasing the ASSETS-relative path and swapping
        // the extension to .png - the tileIcon's actual filename does not always match the apiName
        // (e.g. Rhaast's HUD icon is stored as TFT17_Kayn_Slay_Square.tex, sharing Kayn's alternate-form assets).
        String assetPath = tileIconPath.toLowerCase().replace(".tex", ".png");

        byte[] iconBytes = communityDragonWebClient.fetchChampionIcon(assetPath).block();
        if (iconBytes == null || iconBytes.length == 0)
        {
            throw new IllegalStateException("Community Dragon returned no image data for " + apiName);
        }

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType("image/png")
                        .cacheControl(CACHE_CONTROL)
                        .build(),
                RequestBody.fromBytes(iconBytes));
    }

    private boolean objectExists(String key)
    {
        try
        {
            s3Client.headObject(HeadObjectRequest.builder().bucket(bucketName).key(key).build());
            return true;
        } catch (NoSuchKeyException e)
        {
            return false;
        }
    }

    private String iconKey(String apiName)
    {
        return "set" + setContextService.getCurrentSetNumber() + "/" + championSlug(apiName) + ".png";
    }

    /**
     * Strips the "TFT{setNumber}_" prefix from an apiName (e.g. "TFT17_Nami" -> "nami").
     * Falls back to the full lowercased apiName if it doesn't match the expected pattern.
     */
    private String championSlug(String apiName)
    {
        int underscoreIndex = apiName.indexOf('_');
        String slug = underscoreIndex >= 0 ? apiName.substring(underscoreIndex + 1) : apiName;
        return slug.toLowerCase();
    }
}
