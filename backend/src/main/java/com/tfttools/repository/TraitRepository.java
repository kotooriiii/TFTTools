package com.tfttools.repository;

import com.tfttools.domain.Trait;
import com.tfttools.domain.communitydragon.CommunityDragonObject;
import com.tfttools.domain.communitydragon.CommunityDragonTraitEffects;
import com.tfttools.domain.communitydragon.CommunityDragonTraits;
import com.tfttools.prefixtrie.PrefixTrie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tfttools.service.CommunityDragonDataService;

import static com.tfttools.repository.EmblemRepository.SET_EXAMPLE_NUMBER;

@Component
public class TraitRepository {
    
    private final Map<String, Trait> traits;

    private final PrefixTrie<Trait> traitPrefixTrie;

    private final CommunityDragonDataService dataService;

    @Autowired
    public TraitRepository(CommunityDragonDataService dataService) {
        this.traits = new HashMap<>();

        this.traitPrefixTrie = new PrefixTrie<>();

        this.dataService = dataService;
        
        loadTraits(SET_EXAMPLE_NUMBER);
        this.traits.values().forEach(this.traitPrefixTrie::add);

    }

    private void loadTraits(String set) {
        try {
            CommunityDragonObject communityDragonObject = dataService.getCommunityDragonData();
            List<CommunityDragonTraits> set15Traits = communityDragonObject.getSets().get(set).getTraits();
            
            for (CommunityDragonTraits communityDragonTrait : set15Traits) {
                String apiName = communityDragonTrait.getApiName();
                String name = communityDragonTrait.getName();
                List<CommunityDragonTraitEffects> effects = communityDragonTrait.getEffects();

                // Skip mechanic traits
                if (apiName.contains("MechanicTrait")) {
                    continue;
                }
                if(effects.isEmpty() && !apiName.contains("UndeterminedTrait")) //todo MF is Conduit/Challenger/Replicator
                    continue;

                Trait trait = new Trait(apiName, name,
                        effects.stream().mapToInt(CommunityDragonTraitEffects::getMinUnits).toArray(), 
                        effects.stream().map(CommunityDragonTraitEffects::getStyle).toList());
                this.traits.put(name, trait);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load traits", e);
        }
    }
    public void reloadTraits() {

        loadTraits(SET_EXAMPLE_NUMBER);
    }

    public List<Trait> getAllTraits() {
        return this.traits.values().stream().toList();
    }

    public Trait getTraitByName(String traitName) {
        return this.traits.get(traitName);
    }

    /**
     * Gets all traits starting with a given prefix
     *
     * @param prefix The prefix to be searched for
     * @return List of traits
     */
    public List<Trait> getAllTraitsStartingWith(String prefix)
    {
        return this.traitPrefixTrie.getAllDescendantsByPrefix(prefix);
    }
}
