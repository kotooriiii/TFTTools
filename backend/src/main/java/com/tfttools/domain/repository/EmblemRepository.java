package com.tfttools.domain.repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfttools.domain.Emblem;
import com.tfttools.domain.Trait;
import com.tfttools.domain.repository.communitydragon.CommunityDragonItems;
import com.tfttools.domain.repository.communitydragon.CommunityDragonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EmblemRepository
{
    private final Map<String, Emblem> emblems;
    private final TraitRepository traitRepository;

    @Autowired
    public EmblemRepository(TraitRepository traitRepository)
    {
        this.emblems = new HashMap<>();
        this.traitRepository = traitRepository;

        parseJson("16");
    }

    private void parseJson(String set) { //todo too much dupe code in these repoisotyr classes
        try {
            InputStream inputStream = getClass().getResourceAsStream("/com/tfttools/domain/repository/communitydragon/en_us.json");
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            CommunityDragonObject communityDragonObject = objectMapper.readValue(inputStream, CommunityDragonObject.class);

            List<CommunityDragonItems> communityDragonItemsList = communityDragonObject.getItems();
            for (CommunityDragonItems communityDragonItems : communityDragonItemsList) {
                String apiName = communityDragonItems.getApiName();
                String name = communityDragonItems.getName();

                // skip none emblems
                if(!apiName.startsWith("TFT" + set))
                    continue;
                if (!apiName.endsWith("EmblemItem")) {
                    continue;
                }

                String traitName = communityDragonItems.getName().substring(0, communityDragonItems.getName().indexOf(' '));
                Trait trait = traitRepository.getTraitByName(traitName);

                if(trait == null)
                {
                    throw new RuntimeException("Trait not found: " + traitName);
                }

                Emblem emblem = new Emblem(name,trait);
                this.emblems.put(name, emblem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Emblem> getAllEmblems()
    {
        return this.emblems.values().stream().toList();
    }

    public Emblem getEmblemByName(String emblemName)
    {
        return this.emblems.get(emblemName);
    }
}
