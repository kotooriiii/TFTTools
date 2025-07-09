package com.tfttools.registry;

import com.tfttools.prefixtrie.PrefixTrie;
import com.tfttools.domain.Champion;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.dto.FilterDTO;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.tfttools.domain.Champion.*;
import static com.tfttools.domain.Trait.*;

/**
 * Initialize Champions and their respective traits
 */
@Component
public class UnitRegistry {
    private final Map<Trait, List<Unit>> traitToUnits;
    private final Map<Champion, Unit> championMap;
    private final PrefixTrie<Champion> championPrefixTrie;
    private final PrefixTrie<Trait> traitPrefixTrie;

    public UnitRegistry() {
        Map<Trait, List<Unit>> tempTraitToUnits = new HashMap<>();
        Map<Champion, Unit> tempChampionMap = new HashMap<>();
        this.championPrefixTrie = new PrefixTrie<>();
        this.traitPrefixTrie = new PrefixTrie<>();

        register(tempTraitToUnits, tempChampionMap, ALISTAR, 1, GOLDEN_OX, BRUISER);
        register(tempTraitToUnits, tempChampionMap, ANNIE, 4, GOLDEN_OX, AMP);
        register(tempTraitToUnits, tempChampionMap, APHELIOS, 4, GOLDEN_OX, MARKSMAN);
        register(tempTraitToUnits, tempChampionMap, AURORA, 5, ANIMA_SQUAD, DYNAMO);
        register(tempTraitToUnits, tempChampionMap, BRAND, 4, STREET_DEMON, TECHIE);
        register(tempTraitToUnits, tempChampionMap, BRAUM, 3, SYNDICATE, VANGUARD);
        register(tempTraitToUnits, tempChampionMap, CHO_GATH, 4, BOOMBOT, BRUISER);
        register(tempTraitToUnits, tempChampionMap, DARIUS, 2, SYNDICATE, BRUISER);
        register(tempTraitToUnits, tempChampionMap, DR_MUNDO, 1, STREET_DEMON, BRUISER, SLAYER);
        register(tempTraitToUnits, tempChampionMap, DRAVEN, 3, CYPHER, RAPIDFIRE);
        register(tempTraitToUnits, tempChampionMap, EKKO, 2, STREET_DEMON, STRATEGIST);
        register(tempTraitToUnits, tempChampionMap, ELISE, 3, NITRO, DYNAMO);
        register(tempTraitToUnits, tempChampionMap, FIDDLESTICKS, 3, BOOMBOT, TECHIE);
        register(tempTraitToUnits, tempChampionMap, GALIO, 3, CYPHER, BASTION);
        register(tempTraitToUnits, tempChampionMap, GAREN, 5, GOD_OF_THE_NET);
        register(tempTraitToUnits, tempChampionMap, GRAGAS, 3, DIVINICORP, BRUISER);
        register(tempTraitToUnits, tempChampionMap, GRAVES, 2, GOLDEN_OX, EXECUTIONER);
        register(tempTraitToUnits, tempChampionMap, ILLAOI, 2, ANIMA_SQUAD, BASTION);
        register(tempTraitToUnits, tempChampionMap, JARVAN_IV, 3, GOLDEN_OX, VANGUARD, SLAYER);
        register(tempTraitToUnits, tempChampionMap, JAX, 1, EXOTECH, BASTION);
        register(tempTraitToUnits, tempChampionMap, JHIN, 2, EXOTECH, MARKSMAN, DYNAMO);
        register(tempTraitToUnits, tempChampionMap, JINX, 3, STREET_DEMON, MARKSMAN);
        register(tempTraitToUnits, tempChampionMap, KINDRED, 1, NITRO, RAPIDFIRE, MARKSMAN);
        register(tempTraitToUnits, tempChampionMap, KOBUKO, 5, CYBERBOSS, BRUISER);
        register(tempTraitToUnits, tempChampionMap, KOGMAW, 1, BOOMBOT, RAPIDFIRE);
        register(tempTraitToUnits, tempChampionMap, LEBLANC, 2, CYPHER, STRATEGIST);
        register(tempTraitToUnits, tempChampionMap, LEONA, 4, ANIMA_SQUAD, VANGUARD);
        register(tempTraitToUnits, tempChampionMap, MISS_FORTUNE, 4, SYNDICATE, DYNAMO);
        register(tempTraitToUnits, tempChampionMap, MORDEKAISER, 3, EXOTECH, BRUISER, TECHIE);
        register(tempTraitToUnits, tempChampionMap, MORGANA, 1, DIVINICORP, DYNAMO);
        register(tempTraitToUnits, tempChampionMap, NAAFIRI, 2, EXOTECH, AMP);
        register(tempTraitToUnits, tempChampionMap, NEEKO, 4, STREET_DEMON, STRATEGIST);
        register(tempTraitToUnits, tempChampionMap, NIDALEE, 1, NITRO, AMP);
        register(tempTraitToUnits, tempChampionMap, POPPY, 1, CYBERBOSS, BASTION);
        register(tempTraitToUnits, tempChampionMap, RENEKTON, 5, BASTION, OVERLORD);
        register(tempTraitToUnits, tempChampionMap, RENGAR, 3, STREET_DEMON, EXECUTIONER);
        register(tempTraitToUnits, tempChampionMap, RHAAST, 2, DIVINICORP, VANGUARD);
        register(tempTraitToUnits, tempChampionMap, SAMIRA, 5, STREET_DEMON, AMP);
        register(tempTraitToUnits, tempChampionMap, SEJUANI, 4, EXOTECH, BASTION);
        register(tempTraitToUnits, tempChampionMap, SENNA, 3, SLAYER, DIVINICORP);
        register(tempTraitToUnits, tempChampionMap, SERAPHINE, 1, ANIMA_SQUAD, TECHIE);
        register(tempTraitToUnits, tempChampionMap, SHACO, 1, SYNDICATE, SLAYER);
        register(tempTraitToUnits, tempChampionMap, SHYVANA, 2, NITRO, BASTION, TECHIE);
        register(tempTraitToUnits, tempChampionMap, SKARNER, 2, BOOMBOT, VANGUARD);
        register(tempTraitToUnits, tempChampionMap, SYLAS, 1, ANIMA_SQUAD, VANGUARD);
        register(tempTraitToUnits, tempChampionMap, TWISTED_FATE, 2, SYNDICATE, RAPIDFIRE);
        register(tempTraitToUnits, tempChampionMap, URGOT, 5, BOOMBOT, EXECUTIONER);
        register(tempTraitToUnits, tempChampionMap, VI, 1, CYPHER, VANGUARD);
        register(tempTraitToUnits, tempChampionMap, VIEGO, 5, TECHIE, GOLDEN_OX);
        register(tempTraitToUnits, tempChampionMap, VEX, 4, DIVINICORP, EXECUTIONER);
        register(tempTraitToUnits, tempChampionMap, VAYNE, 2, ANIMA_SQUAD, SLAYER);
        register(tempTraitToUnits, tempChampionMap, VARUS, 3, EXOTECH, EXECUTIONER);
        register(tempTraitToUnits, tempChampionMap, VEIGAR, 2, CYBERBOSS, TECHIE);
        register(tempTraitToUnits, tempChampionMap, XAYAH, 4, ANIMA_SQUAD, MARKSMAN);
        register(tempTraitToUnits, tempChampionMap, YUUMI, 3, ANIMA_SQUAD, AMP, STRATEGIST);
        register(tempTraitToUnits, tempChampionMap, ZAC, 5, VIRUS);
        register(tempTraitToUnits, tempChampionMap, ZED, 4, CYPHER, SLAYER);
        register(tempTraitToUnits, tempChampionMap, ZERI, 4, EXOTECH, RAPIDFIRE);
        register(tempTraitToUnits, tempChampionMap, ZIGGS, 4, CYBERBOSS, STRATEGIST);
        register(tempTraitToUnits, tempChampionMap, ZYRA, 1, STREET_DEMON, TECHIE);

        // Initialize Prefix Tries
        initPrefixTrie();

        // Make all trait lists unmodifiable
        for (Map.Entry<Trait, List<Unit>> entry : tempTraitToUnits.entrySet()) {
            entry.setValue(Collections.unmodifiableList(entry.getValue()));
        }

        // Wrap outer maps as unmodifiable
        traitToUnits = Collections.unmodifiableMap(tempTraitToUnits);
        championMap = Collections.unmodifiableMap(tempChampionMap);
    }

    private void register(Map<Trait, List<Unit>> traitToUnits, Map<Champion, Unit> championMap, Champion champ, int cost, Trait... traits) {
        Unit unit = new Unit(champ, cost, List.of(traits));
        championMap.put(champ, unit);
        for (Trait t : traits) {
            traitToUnits.computeIfAbsent(t, k -> new ArrayList<>()).add(unit);
        }
    }

    private void initPrefixTrie() {
        Arrays.stream(Champion.values()).forEach(this.championPrefixTrie::add);
        Arrays.stream(Trait.values()).forEach(this.traitPrefixTrie::add);
    }

    /**
     * Gets all units grouped by trait
     *
     * @return Map of all units grouped by trait
     */
    public Map<Trait, List<Unit>> getUnitsByTrait() {
        return this.traitToUnits;
    }

    public List<Unit> getUnitsByTrait(Trait trait) {
        return traitToUnits.getOrDefault(trait, new ArrayList<>());
    }

    public Unit getUnitByChampion(Champion champion) {
        return championMap.get(champion);
    }

    /**
     * Gets all units in the {@link UnitRegistry}
     *
     * @return List of all units in the {@link UnitRegistry}
     */
    public List<Unit> getAllUnits() {
        return new ArrayList<>(championMap.values());
    }

    /**
     * Gets all champions starting with a given prefix
     *
     * @param prefix The prefix to be searched for
     * @return List of champions
     */
    public List<Champion> getAllChampionsStartingWith(String prefix) {
        return this.championPrefixTrie.getAllDescendantsByPrefix(prefix);
    }

    /**
     * Gets all traits starting with a given prefix
     *
     * @param prefix The prefix to be searched for
     * @return List of traits
     */
    public List<Trait> getAllTraitsStartingWith(String prefix) {
        return this.traitPrefixTrie.getAllDescendantsByPrefix(prefix);
    }

    /**
     * Gets units according to filter params in FilterDTO
     *
     * @param filterDTO Contains filter data {@link FilterDTO}
     * @return List of units {@link Unit}
     */
    public List<Unit> filter(FilterDTO filterDTO) {
        Set<Unit> filteredUnits = new HashSet<>(getAllUnits());

        if (!filterDTO.getChampions().isEmpty()) {
            // From championDTOList map displayName -> actual Champion
            List<Champion> championList = filterDTO.getChampions().stream().map(championDTO -> Champion.fromDisplayName(championDTO.getDisplayName())).toList();

            // For each champion in championList map Champion -> SingletonSet(Unit)
            List<Set<Unit>> unitsFromChampions = championList.stream().map(champion -> Collections.singleton(getUnitByChampion(champion))).toList();

            // For each unit take intersection of filteredUnits and UnitSingleton
            unitsFromChampions.forEach(filteredUnits::retainAll);

        }

        if (!filterDTO.getTraits().isEmpty()) {
            // From traitList map displayName -> Trait
            List<Trait> traitList = filterDTO.getTraits().stream().map(traitDTO -> Trait.fromDisplayName(traitDTO.getDisplayName())).toList();
            // For each trait, take intersection of filteredUnits and Set(all units in trait)
            traitList.forEach(trait -> filteredUnits.retainAll(new HashSet<>(getUnitsByTrait(trait))));
        }

        return filteredUnits.stream().toList();
    }
}
