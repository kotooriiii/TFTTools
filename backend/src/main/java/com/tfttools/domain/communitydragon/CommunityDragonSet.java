package com.tfttools.domain.communitydragon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CommunityDragonSet {
    @JsonProperty(value = "champions")
    private List<CommunityDragonChampions> champions;

    @JsonProperty(value = "traits")
    private List<CommunityDragonTraits> traits;
}
