package com.tfttools.domain.repository.communitydragon;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class CommunityDragonObject {
    private Map<String, CommunityDragonSet> sets;

}
