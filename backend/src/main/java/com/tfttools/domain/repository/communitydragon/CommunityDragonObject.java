package com.tfttools.domain.repository.communitydragon;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class CommunityDragonObject {
    private Map<String, CommunityDragonSet> sets;
    private List<CommunityDragonItems> items;
}
