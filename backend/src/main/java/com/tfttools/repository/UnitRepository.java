package com.tfttools.repository;

import com.tfttools.domain.Role;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.domain.communitydragon.ChampionStats;
import com.tfttools.domain.communitydragon.CommunityDragonChampions;
import com.tfttools.domain.communitydragon.CommunityDragonObject;
import com.tfttools.prefixtrie.PrefixTrie;
import com.tfttools.service.ChampionIconCacheService;
import com.tfttools.service.CommunityDragonDataService;
import com.tfttools.service.TFTSetContextService;
import com.tfttools.setrules.SetSpecificRules;
import com.tfttools.setrules.SetSpecificRulesRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Repository for managing TFT Units loaded from Community Dragon data
 */
@Component
@DependsOn("TFTSetContextService")
public class UnitRepository
{

    // Matches a variant champion's display name, e.g. "Lux (Inferno)" -> base name "Lux"
    private static final Pattern VARIANT_NAME_PATTERN = Pattern.compile("^(.+?)\\s*\\([^)]*\\)$");

    private final Map<String, Unit> units;

    private final PrefixTrie<Unit> unitPrefixTrie;
    private Map<Trait, List<Unit>> traitToUnits;
    private Map<String, Unit> variantApiNameToBaseUnit;

    private final TraitRepository traitRepository;
    private final CommunityDragonDataService dataService;
    private final TFTSetContextService setContextService;
    private final ChampionIconCacheService championIconCacheService;
    private final SetSpecificRulesRegistry setSpecificRulesRegistry;

    @Autowired
    public UnitRepository(TraitRepository traitRepository, CommunityDragonDataService dataService, TFTSetContextService setContextService, ChampionIconCacheService championIconCacheService, SetSpecificRulesRegistry setSpecificRulesRegistry)
    {
        this.units = new HashMap<>();
        this.unitPrefixTrie = new PrefixTrie<>();

        this.traitRepository = traitRepository;
        this.dataService = dataService;
        this.setContextService = setContextService;
        this.championIconCacheService = championIconCacheService;
        this.setSpecificRulesRegistry = setSpecificRulesRegistry;
    }

    @PostConstruct
    public void init()
    {
        loadUnits(setContextService.getCurrentSetNumber());
        this.units.values().forEach(this.unitPrefixTrie::add);

        registerTraitToUnitsMapping();

        championIconCacheService.ensureAllCached(getAllUnits());
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
            SetSpecificRules setRules = setSpecificRulesRegistry.getRulesFor(set);

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

                Map<Trait, Integer> traitCountOverridesByName = setRules.getTraitCountOverrides().getOrDefault(apiName, Map.of());

                this.units.put(name, new Unit(apiName, name, cost, role, championStats, traits, champions.getTileIcon(), traitCountOverridesByName));
            }

            linkVariantUnits(setRules);
        } catch (Exception e)
        {
            throw new RuntimeException("Failed to load units", e);
        }
    }

    /**
     * Links trait-variant champions (e.g. "Lux (Inferno)") back to their base champion
     * (e.g. "Lux"), keyed by the variant's apiName. Consumers that need a variant's data to
     * fall back to its base champion (e.g. team planner codes, which are only ever tracked
     * per base champion) can resolve that link via {@link #getBaseUnit(Unit)}.
     */
    private void linkVariantUnits(SetSpecificRules setRules)
    {
        Map<String, Unit> variantMap = new HashMap<>();

        for (Unit unit : this.units.values())
        {
            String baseName = setRules.resolveVariantBaseName(unit.getDisplayName())
                    .orElseGet(() -> matchGenericVariantBaseName(unit.getDisplayName()));

            if (baseName == null)
            {
                continue;
            }

            Unit baseUnit = this.units.get(baseName.trim());
            if (baseUnit != null && baseUnit != unit)
            {
                variantMap.put(unit.getApiName(), baseUnit);
            }
        }

        this.variantApiNameToBaseUnit = Collections.unmodifiableMap(variantMap);
    }

    /**
     * Generic fallback for variant name resolution: matches the common "Base (Variant)" naming
     * convention. Used when the current set has no {@link SetSpecificRules} override for it.
     */
    private String matchGenericVariantBaseName(String displayName)
    {
        Matcher matcher = VARIANT_NAME_PATTERN.matcher(displayName);
        return matcher.matches() ? matcher.group(1) : null;
    }

    /**
     * Gets the base champion a trait-variant champion belongs to, if any.
     *
     * @param unit The unit to resolve
     * @return The base unit, or empty if the given unit is not a recognized variant
     */
    public Optional<Unit> getBaseUnit(Unit unit)
    {
        return Optional.ofNullable(this.variantApiNameToBaseUnit.get(unit.getApiName()));
    }

    public void reloadUnits()
    {
        loadUnits(setContextService.getCurrentSetNumber());
    }

    /**
     * Gets all units grouped by trait
     *
     * @return List of all units grouped by trait
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
     * Gets all units starting with a given prefix
     *
     * @param prefix The prefix to be searched for
     * @return List of units
     */
    public List<Unit> getAllChampionsStartingWith(String prefix)
    {
        return this.unitPrefixTrie.getAllDescendantsByPrefix(prefix);
    }

}
