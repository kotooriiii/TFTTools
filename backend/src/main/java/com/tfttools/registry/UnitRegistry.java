package com.tfttools.registry;

import com.tfttools.PrefixTrie.PrefixTrie;
import com.tfttools.domain.Champion;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import org.springframework.stereotype.Component;
import java.util.*;
import static com.tfttools.domain.Champion.*;
import static com.tfttools.domain.Trait.*;

/**
 * Initialize Champions and their respective traits
 */
@Component
public class UnitRegistry
{
    private final Map<Trait, List<Unit>> traitToUnits;
    private final Map<Champion, Unit> championMap;
    private final PrefixTrie<Champion> championPrefixTrie;
    private final PrefixTrie<Trait> traitPrefixTrie;

    public UnitRegistry()
    {
        Map<Trait, List<Unit>> tempTraitToUnits = new HashMap<>();
        Map<Champion, Unit> tempChampionMap = new HashMap<>();
        this.championPrefixTrie = new PrefixTrie<>();
        this.traitPrefixTrie = new PrefixTrie<>();

        register(tempTraitToUnits, tempChampionMap, ALISTAR, GOLDEN_OX, BRUISER);
        register(tempTraitToUnits, tempChampionMap, ANNIE, GOLDEN_OX, AMP);
        register(tempTraitToUnits, tempChampionMap, APHELIOS, GOLDEN_OX, MARKSMAN);
        register(tempTraitToUnits, tempChampionMap, AURORA, ANIMA_SQUAD, DYNAMO);
        register(tempTraitToUnits, tempChampionMap, BRAND, STREET_DEMON, TECHIE);
        register(tempTraitToUnits, tempChampionMap, BRAUM, SYNDICATE, VANGUARD);
        register(tempTraitToUnits, tempChampionMap, CHO_GATH, BOOMBOTS, BRUISER);
        register(tempTraitToUnits, tempChampionMap, DARIUS, SYNDICATE, BRUISER);
        register(tempTraitToUnits, tempChampionMap, DR_MUNDO, STREET_DEMON, BRUISER, SLAYER);
        register(tempTraitToUnits, tempChampionMap, DRAVEN, CYPHER, RAPIDFIRE);
        register(tempTraitToUnits, tempChampionMap, EKKO, STREET_DEMON, STRATEGIST);
        register(tempTraitToUnits, tempChampionMap, ELISE, NITRO, DYNAMO);
        register(tempTraitToUnits, tempChampionMap, FIDDLESTICKS, BOOMBOTS, TECHIE);
        register(tempTraitToUnits, tempChampionMap, GALIO, CYPHER, BASTION);
        register(tempTraitToUnits, tempChampionMap, GAREN, GOD_OF_THE_NET);
        register(tempTraitToUnits, tempChampionMap, GRAGAS, DIVINICORP, BRUISER);
        register(tempTraitToUnits, tempChampionMap, GRAVES, GOLDEN_OX, EXECUTIONER);
        register(tempTraitToUnits, tempChampionMap, ILLAOI, ANIMA_SQUAD, BASTION);
        register(tempTraitToUnits, tempChampionMap, JARVAN_IV, GOLDEN_OX, VANGUARD, SLAYER);
        register(tempTraitToUnits, tempChampionMap, JAX, EXOTECH, BASTION);
        register(tempTraitToUnits, tempChampionMap, JHIN, EXOTECH, MARKSMAN, DYNAMO);
        register(tempTraitToUnits, tempChampionMap, JINX, STREET_DEMON, MARKSMAN);
        register(tempTraitToUnits, tempChampionMap, KINDRED, NITRO, RAPIDFIRE, MARKSMAN);
        register(tempTraitToUnits, tempChampionMap, KOBUKO, CYBERBOSS, BRUISER);
        register(tempTraitToUnits, tempChampionMap, KOGMAW, BOOMBOTS, RAPIDFIRE);
        register(tempTraitToUnits, tempChampionMap, LEBLANC, CYPHER, STRATEGIST);
        register(tempTraitToUnits, tempChampionMap, LEONA, ANIMA_SQUAD, VANGUARD);
        register(tempTraitToUnits, tempChampionMap, MISS_FORTUNE, SYNDICATE, DYNAMO);
        register(tempTraitToUnits, tempChampionMap, MORDEKAISER, EXOTECH, BRUISER, TECHIE);
        register(tempTraitToUnits, tempChampionMap, MORGANA, DIVINICORP, DYNAMO);
        register(tempTraitToUnits, tempChampionMap, NAAFIRI, EXOTECH, AMP);
        register(tempTraitToUnits, tempChampionMap, NEEKO, STREET_DEMON, STRATEGIST);
        register(tempTraitToUnits, tempChampionMap, NIDALEE, NITRO, AMP);
        register(tempTraitToUnits, tempChampionMap, POPPY, CYBERBOSS, BASTION);
        register(tempTraitToUnits, tempChampionMap, RENEKTON, BASTION, OVERLORD);
        register(tempTraitToUnits, tempChampionMap, RENGAR, STREET_DEMON, EXECUTIONER);
        register(tempTraitToUnits, tempChampionMap, RHAAST, DIVINICORP, VANGUARD);
        register(tempTraitToUnits, tempChampionMap, SHACO, SYNDICATE, SLAYER);
        register(tempTraitToUnits, tempChampionMap, SHYVANA, NITRO, BASTION, TECHIE);
        register(tempTraitToUnits, tempChampionMap, SKARNER, BOOMBOTS, VANGUARD);
        register(tempTraitToUnits, tempChampionMap, SYLAS, ANIMA_SQUAD, VANGUARD);
        register(tempTraitToUnits, tempChampionMap, VI, CYPHER, VANGUARD);
        register(tempTraitToUnits, tempChampionMap, VEX, DIVINICORP, EXECUTIONER);
        register(tempTraitToUnits, tempChampionMap, VAYNE, ANIMA_SQUAD, SLAYER);
        register(tempTraitToUnits, tempChampionMap, VARUS, EXOTECH, EXECUTIONER);
        register(tempTraitToUnits, tempChampionMap, VEIGAR, CYBERBOSS, TECHIE);
        register(tempTraitToUnits, tempChampionMap, XAYAH, ANIMA_SQUAD, MARKSMAN);
        register(tempTraitToUnits, tempChampionMap, YUUMI, ANIMA_SQUAD, AMP, STRATEGIST);
        register(tempTraitToUnits, tempChampionMap, ZAC, VIRUS);
        register(tempTraitToUnits, tempChampionMap, ZED, CYPHER, SLAYER);
        register(tempTraitToUnits, tempChampionMap, ZERI, EXOTECH, RAPIDFIRE);
        register(tempTraitToUnits, tempChampionMap, ZIGGS, CYBERBOSS, STRATEGIST);
        register(tempTraitToUnits, tempChampionMap, ZYRA, STREET_DEMON, TECHIE);

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

    private void register(Map<Trait, List<Unit>> traitToUnits, Map<Champion, Unit> championMap, Champion champ, Trait... traits) {
        Unit unit = new Unit(champ, List.of(traits));
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
     * @return Map of all units grouped by trait
     */
    public Map<Trait, List<Unit>> getUnitsByTrait()
    {
        return this.traitToUnits;
    }

    public List<Unit> getUnitsByTrait(Trait trait)
    {
        return traitToUnits.getOrDefault(trait, new ArrayList<>());
    }
    public Unit getUnitByChampion(Champion champion)
    {
        return championMap.get(champion);
    }

    /**
     * Gets all units in the {@link UnitRegistry}
     * @return List of all units in the {@link UnitRegistry}
     */
    public List<Unit> getAllUnits()
    {
        return new ArrayList<>(championMap.values());
    }

    /**
     * Gets all champions starting with a given prefix
     * @param prefix The prefix to be searched for
     * @return List of champions
     */
    public List<Champion> getAllChampionsStartingWith(String prefix) {
        return this.championPrefixTrie.getAllDescendantsByPrefix(prefix);
    }

    /**
     * Gets all traits starting with a given prefix
     * @param prefix The prefix to be searched for
     * @return List of traits
     */
    public List<Trait> getAllTraitsStartingWith(String prefix) {
        return this.traitPrefixTrie.getAllDescendantsByPrefix(prefix);
    }
}
