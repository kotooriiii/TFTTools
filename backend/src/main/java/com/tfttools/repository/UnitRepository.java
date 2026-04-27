package com.tfttools.repository;

import com.tfttools.domain.Role;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.domain.communitydragon.ChampionStats;
import com.tfttools.domain.communitydragon.CommunityDragonChampions;
import com.tfttools.domain.communitydragon.CommunityDragonObject;
import com.tfttools.prefixtrie.PrefixTrie;
import com.tfttools.service.CommunityDragonDataService;
import com.tfttools.service.TFTSetContextService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Repository for managing TFT Units loaded from Community Dragon data
 */
@Component
@DependsOn("TFTSetContextService")
public class UnitRepository
{

    private final Map<String, Unit> units;

    private final PrefixTrie<Unit> unitPrefixTrie;
    private Map<Trait, List<Unit>> traitToUnits;

    private final TraitRepository traitRepository;
    private final CommunityDragonDataService dataService;
    private final TFTSetContextService setContextService;

    @Autowired
    public UnitRepository(TraitRepository traitRepository, CommunityDragonDataService dataService, TFTSetContextService setContextService)
    {
        this.units = new HashMap<>();
        this.unitPrefixTrie = new PrefixTrie<>();

        this.traitRepository = traitRepository;
        this.dataService = dataService;
        this.setContextService = setContextService;
    }

    @PostConstruct
    public void init()
    {
        loadUnits(setContextService.getCurrentSetNumber());
        this.units.values().forEach(this.unitPrefixTrie::add);

        registerTraitToUnitsMapping();
    }

    private void registerTraitToUnitsMapping()
    {
        Map<Trait, List<Unit>> tempTraitToUnits = new HashMap<>();

        for (Unit unit : getAllUnits())
        {
            for (Trait trait : unit.getTraits())
            {
                tempTraitToUnits.computeIfAbsent(trait, k -> new ArrayList<>()).add(unit);
            }
        }

        // Make all trait lists unmodifiable
        tempTraitToUnits.replaceAll((trait, units) -> Collections.unmodifiableList(units));

        // Now assign the immutable map
        this.traitToUnits = Collections.unmodifiableMap(tempTraitToUnits);
    }


    private void loadUnits(String set)
    {
        try
        {
            CommunityDragonObject communityDragonObject = dataService.getCommunityDragonData();
            List<CommunityDragonChampions> units = communityDragonObject.getSets().get(set).getChampions();

            for (CommunityDragonChampions champions : units)
            {
                String apiName = champions.getApiName();
                String name = champions.getName().trim();
                int cost = champions.getCost();
                Role role = Role.getRoleFromDisplayName(champions.getRole());
                ChampionStats championStats = champions.getStats();
                List<Trait> traits = champions.getTraits().stream()
                        .map(traitRepository::getTraitByName)
                        .toList();

                if (traits.isEmpty())
                {
                    continue;
                }

                this.units.put(name, new Unit(apiName, name, cost, role, championStats, traits));
            }
        } catch (Exception e)
        {
            throw new RuntimeException("Failed to load units", e);
        }
    }

    public void reloadUnits()
    {
        loadUnits(setContextService.getCurrentSetNumber());
    }

    /**
     * Gets all units grouped by trait
     *
     * @return Map of all units grouped by trait
     */
    public List<Unit> getUnitsByTrait(Trait trait)
    {
        return traitToUnits.getOrDefault(trait, Collections.emptyList());
    }

    /**
     * Gets all units grouped by trait from only the allowed availableUnits
     *
     * @return List of units grouped by the specified trait that are in availableUnits
     */
    public List<Unit> getUnitsByTrait(Trait trait, Set<Unit> availableUnits)
    {
        return traitToUnits.getOrDefault(trait, Collections.emptyList())
                .stream()
                .filter(availableUnits::contains)
                .toList();
    }


    public Unit getUnitByName(String unit)
    {
        return this.units.get(unit);
    }

    public Set<Unit> getAllUnits()
    {
        return new HashSet<>(this.units.values());
    }

    /**
     * Gets all champions starting with a given prefix
     *
     * @param prefix The prefix to be searched for
     * @return List of champions
     */
    public List<Unit> getAllChampionsStartingWith(String prefix)
    {
        return this.unitPrefixTrie.getAllDescendantsByPrefix(prefix);
    }

}
