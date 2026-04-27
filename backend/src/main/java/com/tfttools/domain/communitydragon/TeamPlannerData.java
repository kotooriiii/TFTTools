package com.tfttools.domain.communitydragon;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TeamPlannerData {

    // Use @JsonAnySetter to capture dynamic keys like "TFTSet16", "TFTSet15", etc.
    private Map<String, List<TeamPlannerChampion>> sets = new HashMap<>();

    @JsonAnySetter
    public void setDynamicProperty(String key, List<TeamPlannerChampion> value) {
        sets.put(key, value);
    }

    public List<TeamPlannerChampion> getSetData(String setName) {
        return sets != null ? sets.get(setName) : null;
    }
}