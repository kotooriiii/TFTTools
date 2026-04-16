package com.tfttools.domain.communitydragon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CommunityDragonTraits {
    @JsonProperty(value = "apiName")
    private String apiName;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "effects")
    private List<CommunityDragonTraitEffects> effects;

}
