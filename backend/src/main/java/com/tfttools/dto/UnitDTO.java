package com.tfttools.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.tfttools.domain.Role;
import com.tfttools.domain.Unit;
import com.tfttools.domain.communitydragon.ChampionStats;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

/**
 * Sanitize unit object for data transfer from business logic to requestor
 */
@Getter
public class UnitDTO {
    private final String apiName;
    private final String displayName;
    private final Set<TraitDTO> traits;
    private final ChampionStats championStats;
    private final Role role;
    private final int cost;
    private final String iconUrl;

    public UnitDTO(String apiName, String displayName, Set<TraitDTO> traits, ChampionStats championStats, Role role, int cost, String iconUrl) {
        this.apiName = apiName;
        this.displayName = displayName;
        this.traits = traits;
        this.championStats = championStats;
        this.role = role;
        this.cost = cost;
        this.iconUrl = iconUrl;
    }

    public UnitDTO(String apiName, String displayName, Set<TraitDTO> traits, int cost, String iconUrl) {
        this(apiName, displayName, traits, null, null, cost, iconUrl);
    }

    /**
     * Incoming-request constructor: the frontend identifies units by apiName only
     * ({@code {"apiName": "..."}}); display name isn't needed on the way in.
     */
    @JsonCreator
    public UnitDTO(String apiName) {
        this(apiName, null, null, null, null, 0, null);
    }

}
