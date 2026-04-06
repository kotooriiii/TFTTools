package com.tfttools.domain.repository.communitydragon;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CommunityDragonSet {
    private List<CommunityDragonChampions> champions;
    private List<CommunityDragonTraits> traits;
}
