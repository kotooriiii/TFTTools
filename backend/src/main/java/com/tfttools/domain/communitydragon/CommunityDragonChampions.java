package com.tfttools.domain.communitydragon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CommunityDragonChampions {
    @JsonProperty(value = "apiName")
    private String apiName;

    @JsonProperty(value = "characterName")
    private String characterName;

    @JsonProperty(value = "cost")
    private int cost;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "role")
    private String role;

    @JsonProperty(value = "stats")
    private ChampionStats stats;

    @JsonProperty(value = "traits")
    private List<String> traits;

}
