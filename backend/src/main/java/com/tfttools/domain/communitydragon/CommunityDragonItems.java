package com.tfttools.domain.communitydragon;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CommunityDragonItems
{
    @JsonProperty(value = "apiName")
    private String apiName;

    @JsonProperty("name")
    private String name;

    @JsonProperty(value = "incompatibleTraits")
    private List<String> incompatibleTraits;
}
