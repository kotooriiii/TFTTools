package com.tfttools.domain.repository.communitydragon;


import lombok.Data;

import java.util.List;

@Data
public class CommunityDragonItems
{
    private String apiName;
    private String name;
    private List<String> incompatibleTraits;
}
