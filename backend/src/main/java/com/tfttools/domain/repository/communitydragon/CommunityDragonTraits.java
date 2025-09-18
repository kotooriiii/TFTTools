package com.tfttools.domain.repository.communitydragon;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class CommunityDragonTraits {
    private String apiName;
    private String name;
    private List<CommunityDragonTraitEffects> effects;

}
