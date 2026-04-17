package com.tfttools.domain.communitydragon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class CommunityDragonObject {
    @JsonProperty(value = "sets")
    private Map<String, CommunityDragonSet> sets;

    @JsonProperty(value = "items")
    private List<CommunityDragonItems> items;
}
