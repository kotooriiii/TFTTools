package com.tfttools.repository;

import com.tfttools.domain.Emblem;
import com.tfttools.domain.Trait;
import com.tfttools.domain.communitydragon.CommunityDragonItems;
import com.tfttools.domain.communitydragon.CommunityDragonObject;
import com.tfttools.prefixtrie.PrefixTrie;
import com.tfttools.service.CommunityDragonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EmblemRepository
{

    private final Map<String, Emblem> emblems;

    private final PrefixTrie<Emblem> emblemPrefixTrie;

    private final TraitRepository traitRepository;
    private final CommunityDragonDataService dataService;

    public static String SET_EXAMPLE_NUMBER="17";
    @Autowired
    public EmblemRepository(TraitRepository traitRepository,
                            CommunityDragonDataService dataService)
    {
        this.emblems = new HashMap<>();

        this.emblemPrefixTrie = new PrefixTrie<>();

        this.traitRepository = traitRepository;
        this.dataService = dataService;

        loadEmblems(SET_EXAMPLE_NUMBER);
        emblems.values().forEach(this.emblemPrefixTrie::add);

    }

    private void loadEmblems(String set)
    {
        try
        {
            CommunityDragonObject communityDragonObject = dataService.getCommunityDragonData();
            List<CommunityDragonItems> communityDragonItemsList = communityDragonObject.getItems();

            for (CommunityDragonItems communityDragonItems : communityDragonItemsList)
            {
                String apiName = communityDragonItems.getApiName();
                String name = communityDragonItems.getName();

                // Skip non-emblems
                if (!apiName.startsWith("TFT" + set) || !apiName.endsWith("EmblemItem"))
                {
                    continue;
                }

                String traitName = communityDragonItems.getName().substring(0, communityDragonItems.getName().indexOf(" Emblem"));
                Trait trait = traitRepository.getTraitByName(traitName);

                if (trait == null)
                {
                    throw new RuntimeException("Trait not found: " + traitName);
                }

                Emblem emblem = new Emblem(name, trait);
                this.emblems.put(name, emblem);
            }
        } catch (Exception e)
        {
            throw new RuntimeException("Failed to load emblems", e);
        }
    }

    public void reloadEmblems() {
        loadEmblems(SET_EXAMPLE_NUMBER);
    }

    public List<Emblem> getAllEmblems()
    {
        return this.emblems.values().stream().toList();
    }

    public Emblem getEmblemByName(String emblemName)
    {
        return this.emblems.get(emblemName);
    }

    public List<Emblem> getAllEmblemsStartingWith(String prefix)
    {
        return this.emblemPrefixTrie.getAllDescendantsByPrefix(prefix);
    }
}
