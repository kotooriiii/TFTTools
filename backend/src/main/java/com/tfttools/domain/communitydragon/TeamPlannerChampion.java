package com.tfttools.domain.communitydragon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamPlannerChampion {
    private String path;
    
    @JsonProperty("character_id")
    private String characterId;
    
    private int tier;
    
    @JsonProperty("display_name")
    private String displayName;
    
    @JsonProperty("team_planner_code")
    private int teamPlannerCode;
    
    private List<TeamPlannerTrait> traits;
    
    @JsonProperty("squareIconPath")
    private String squareIconPath;
    
    @JsonProperty("squareSplashIconPath")
    private String squareSplashIconPath;
}
