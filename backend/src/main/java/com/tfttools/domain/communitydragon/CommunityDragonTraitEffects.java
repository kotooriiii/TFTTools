package com.tfttools.domain.communitydragon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommunityDragonTraitEffects {
    @JsonProperty(value = "maxUnits")
    private int maxUnits;

    @JsonProperty(value = "minUnits")
    private int minUnits;

    @JsonProperty(value = "style")
    private int style;

}
