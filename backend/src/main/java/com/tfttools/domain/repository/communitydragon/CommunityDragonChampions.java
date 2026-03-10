package com.tfttools.domain.repository.communitydragon;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CommunityDragonChampions {
    private String apiName;
    private String characterName;
    private int cost;
    private String name;
    private String role;
    private ChampionStats stats;
    private List<String> traits;

}
