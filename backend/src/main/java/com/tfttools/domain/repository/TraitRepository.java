package com.tfttools.domain.repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfttools.domain.Trait;
import com.tfttools.domain.repository.communitydragon.CommunityDragonObject;
import com.tfttools.domain.repository.communitydragon.CommunityDragonTraitEffects;
import com.tfttools.domain.repository.communitydragon.CommunityDragonTraits;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

@Component
public class TraitRepository {
    private final Map<String, Trait> traits;

    public TraitRepository() {
        this.traits = new HashMap<>();

        parseJson("15");
    }

    public TraitRepository(String set) {
        this.traits = new HashMap<>();

        parseJson(set);
    }

    private void parseJson(String set) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/com/tfttools/domain/repository/communitydragon/en_us.json");
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            CommunityDragonObject communityDragonObject = objectMapper.readValue(inputStream, CommunityDragonObject.class);

            List<CommunityDragonTraits> set15Traits = communityDragonObject.getSets().get(set).getTraits();
            for (CommunityDragonTraits communityDragonTrait : set15Traits) {
                String apiName = communityDragonTrait.getApiName();
                String name = communityDragonTrait.getName();
                List<CommunityDragonTraitEffects> effects = communityDragonTrait.getEffects();

                // skip mechanic traits
                if (apiName.contains("MechanicTrait")) {
                    continue;
                }

                List<int[]> thresholds = new ArrayList<>();
                for (CommunityDragonTraitEffects effect : effects) {
                    int[] innerList = new int[] { effect.getMinUnits(), effect.getMaxUnits() };
                    thresholds.add(innerList);
                }
                //todo this.traits.put(name, new Trait(name, thresholds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Trait> getAllTraits() {
        return this.traits.values().stream().toList();
    }

    public Trait getTraitByName(String traitName) {
        return this.traits.get(traitName);
    }
}
