package com.tfttools.registry;

import com.tfttools.domain.*;
import com.tfttools.domain.repository.TraitRepository;
import com.tfttools.domain.repository.UnitRepository;
import com.tfttools.domain.repository.communitydragon.ChampionStats;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.engine.TFTEngine;
import com.tfttools.engine.manager.Manager;
import com.tfttools.prefixtrie.PrefixTrie;
import com.tfttools.dto.FilterDTO;
import org.springframework.stereotype.Component;
import java.util.*;
import static com.tfttools.domain.Champion.*;
import static com.tfttools.domain.Trait.*;

/**
 * Initialize Units and their respective traits
 */
@Component
public class UnitRegistry {
    private Map<Trait, List<Unit>> traitToUnits;
    private final Map<Champion, Unit> championMap;

    private final PrefixTrie<Champion> championPrefixTrie;
    private final PrefixTrie<Trait> traitPrefixTrie;
    private final PrefixTrie<Emblem> emblemPrefixTrie;

    private final TraitRepository traitRepository;
    private final UnitRepository unitRepository;


    public UnitRegistry() {
        this.championPrefixTrie = new PrefixTrie<>();
        this.traitPrefixTrie = new PrefixTrie<>();
        this.emblemPrefixTrie = new PrefixTrie<>();


        this.traitRepository = new TraitRepository();
        this.unitRepository = new UnitRepository(traitRepository);

        Map<Trait, List<Unit>> tempTraitToUnits = new HashMap<>();
        Map<Champion, Unit> tempChampionMap = new HashMap<>();

        // 1-Cost Champions
        register(tempTraitToUnits, tempChampionMap, ANIVIA, FRELJORD, INVOKER);
        register(tempTraitToUnits, tempChampionMap, BLITZCRANK, ZAUN, JUGGERNAUT);
        register(tempTraitToUnits, tempChampionMap, BRIAR, NOXUS, JUGGERNAUT, SLAYER);
        register(tempTraitToUnits, tempChampionMap, CAITLYN, PILTOVER, LONGSHOT);
        register(tempTraitToUnits, tempChampionMap, ILLAOI, BILGEWATER, BRUISER);
        register(tempTraitToUnits, tempChampionMap, JARVAN_IV, DEMACIA, DEFENDER);
        register(tempTraitToUnits, tempChampionMap, JHIN, IONIA, LONGSHOT);
        register(tempTraitToUnits, tempChampionMap, KOG_MAW, VOID, ARCANIST, LONGSHOT);
        register(tempTraitToUnits, tempChampionMap, LULU, YORDLE, ARCANIST);
        register(tempTraitToUnits, tempChampionMap, QIYANA, IXTAL, SLAYER);
        register(tempTraitToUnits, tempChampionMap, RUMBLE, YORDLE, DEFENDER);
        register(tempTraitToUnits, tempChampionMap, SHEN, IONIA, BRUISER);
        register(tempTraitToUnits, tempChampionMap, SONA, DEMACIA, INVOKER);
        register(tempTraitToUnits, tempChampionMap, VIEGO, SHADOW_ISLES, QUICKSTRIKER);

        // 2-Cost Champions
        register(tempTraitToUnits, tempChampionMap, APHELIOS, TARGON);
        register(tempTraitToUnits, tempChampionMap, ASHE, FRELJORD, QUICKSTRIKER);
        register(tempTraitToUnits, tempChampionMap, BARD, CARETAKER);
        register(tempTraitToUnits, tempChampionMap, CHO_GATH, VOID, JUGGERNAUT);
        register(tempTraitToUnits, tempChampionMap, EKKO, ZAUN, DISRUPTOR);
        register(tempTraitToUnits, tempChampionMap, GRAVES, BILGEWATER, GUNSLINGER);
        register(tempTraitToUnits, tempChampionMap, NEEKO, IXTAL, DEFENDER, ARCANIST);
        register(tempTraitToUnits, tempChampionMap, ORIANNA, PILTOVER, INVOKER);
        register(tempTraitToUnits, tempChampionMap, POPPY, DEMACIA, JUGGERNAUT, YORDLE);
        register(tempTraitToUnits, tempChampionMap, REK_SAI, VOID, VANQUISHER);
        register(tempTraitToUnits, tempChampionMap, SION, NOXUS, BRUISER);
        register(tempTraitToUnits, tempChampionMap, TEEMO, YORDLE, LONGSHOT);
        register(tempTraitToUnits, tempChampionMap, TRISTANA, YORDLE, GUNSLINGER);
        register(tempTraitToUnits, tempChampionMap, TRYNDAMERE, FRELJORD, SLAYER);
        register(tempTraitToUnits, tempChampionMap, TWISTED_FATE, BILGEWATER, QUICKSTRIKER);
        register(tempTraitToUnits, tempChampionMap, VI, PILTOVER, ZAUN, DEFENDER);
        register(tempTraitToUnits, tempChampionMap, XIN_ZHAO, DEMACIA, IONIA, WARDEN);
        register(tempTraitToUnits, tempChampionMap, YASUO, IONIA, SLAYER);
        register(tempTraitToUnits, tempChampionMap, YORICK, SHADOW_ISLES, WARDEN);

        // 3-Cost Champions
        register(tempTraitToUnits, tempChampionMap, AHRI, IONIA, ARCANIST);
        register(tempTraitToUnits, tempChampionMap, DARIUS, NOXUS, DEFENDER);
        register(tempTraitToUnits, tempChampionMap, DR_MUNDO, ZAUN, BRUISER);
        register(tempTraitToUnits, tempChampionMap, DRAVEN, NOXUS, QUICKSTRIKER);
        register(tempTraitToUnits, tempChampionMap, GANGPLANK, BILGEWATER, VANQUISHER, SLAYER);
        register(tempTraitToUnits, tempChampionMap, GWEN, SHADOW_ISLES, DISRUPTOR);
        register(tempTraitToUnits, tempChampionMap, JINX, ZAUN, GUNSLINGER);
        register(tempTraitToUnits, tempChampionMap, KENNEN, IONIA, YORDLE, DEFENDER);
        register(tempTraitToUnits, tempChampionMap, KOBUKO_AND_YUUMI, YORDLE, BRUISER, INVOKER);
        register(tempTraitToUnits, tempChampionMap, LEBLANC, NOXUS, INVOKER);
        register(tempTraitToUnits, tempChampionMap, LEONA, TARGON);
        register(tempTraitToUnits, tempChampionMap, LORIS, PILTOVER, WARDEN);
        register(tempTraitToUnits, tempChampionMap, MALZAHAR, VOID, DISRUPTOR);
        register(tempTraitToUnits, tempChampionMap, MILIO, IXTAL, INVOKER);
        register(tempTraitToUnits, tempChampionMap, NAUTILUS, BILGEWATER, JUGGERNAUT, WARDEN);
        register(tempTraitToUnits, tempChampionMap, SEJUANI, FRELJORD, DEFENDER);
        register(tempTraitToUnits, tempChampionMap, VAYNE, DEMACIA, LONGSHOT);
        register(tempTraitToUnits, tempChampionMap, ZOE);

        // 4-Cost Champions
        register(tempTraitToUnits, tempChampionMap, AMBESSA, NOXUS, VANQUISHER);
        register(tempTraitToUnits, tempChampionMap, BEL_VETH, VOID, SLAYER);
        register(tempTraitToUnits, tempChampionMap, BRAUM, FRELJORD, WARDEN);
        register(tempTraitToUnits, tempChampionMap, DIANA, TARGON);
        register(tempTraitToUnits, tempChampionMap, FIZZ, BILGEWATER, YORDLE);
        register(tempTraitToUnits, tempChampionMap, GAREN, DEMACIA, DEFENDER);
        register(tempTraitToUnits, tempChampionMap, KAI_SA, ASSIMILATOR, VOID, LONGSHOT);
        register(tempTraitToUnits, tempChampionMap, KALISTA, SHADOW_ISLES, VANQUISHER);
        register(tempTraitToUnits, tempChampionMap, LISSANDRA, FRELJORD, INVOKER);
        register(tempTraitToUnits, tempChampionMap, LUX, DEMACIA, ARCANIST);
        register(tempTraitToUnits, tempChampionMap, MISS_FORTUNE, BILGEWATER, GUNSLINGER);
        register(tempTraitToUnits, tempChampionMap, NASUS, SHURIMA);
        register(tempTraitToUnits, tempChampionMap, NIDALEE, HUNTRESS, IXTAL);
        register(tempTraitToUnits, tempChampionMap, RENEKTON, SHURIMA);
        register(tempTraitToUnits, tempChampionMap, RIFT_HERALD, VOID, BRUISER);
        register(tempTraitToUnits, tempChampionMap, SERAPHINE, PILTOVER, DISRUPTOR);
        register(tempTraitToUnits, tempChampionMap, SINGED, ZAUN, JUGGERNAUT);
        register(tempTraitToUnits, tempChampionMap, SKARNER, IXTAL);
        register(tempTraitToUnits, tempChampionMap, SWAIN, NOXUS, ARCANIST, JUGGERNAUT);
        register(tempTraitToUnits, tempChampionMap, TARIC, TARGON);
        register(tempTraitToUnits, tempChampionMap, VEIGAR, YORDLE, ARCANIST);
        register(tempTraitToUnits, tempChampionMap, WARWICK, ZAUN, QUICKSTRIKER);
        register(tempTraitToUnits, tempChampionMap, WUKONG, IONIA, BRUISER);
        register(tempTraitToUnits, tempChampionMap, YONE, IONIA, SLAYER);
        register(tempTraitToUnits, tempChampionMap, YUNARA, IONIA, QUICKSTRIKER);

        // 5-Cost Champions
        register(tempTraitToUnits, tempChampionMap, AATROX, DARKIN, WORLD_ENDER, SLAYER);
        register(tempTraitToUnits, tempChampionMap, ANNIE, DARK_CHILD, ARCANIST);
        register(tempTraitToUnits, tempChampionMap, AZIR, EMPEROR, SHURIMA, DISRUPTOR);
        register(tempTraitToUnits, tempChampionMap, FIDDLESTICKS, HARVESTER, VANQUISHER);
        register(tempTraitToUnits, tempChampionMap, GALIO, HEROIC, DEMACIA);
        register(tempTraitToUnits, tempChampionMap, KINDRED, ETERNAL, QUICKSTRIKER);
        register(tempTraitToUnits, tempChampionMap, LUCIAN_AND_SENNA, SOULBOUND, GUNSLINGER);
        register(tempTraitToUnits, tempChampionMap, MEL, NOXUS, DISRUPTOR);
        register(tempTraitToUnits, tempChampionMap, ORNN, BLACKSMITH, WARDEN);
        register(tempTraitToUnits, tempChampionMap, RYZE, RUNE_MAGE);
        register(tempTraitToUnits, tempChampionMap, SETT, THE_BOSS, IONIA);
        register(tempTraitToUnits, tempChampionMap, SHYVANA, DRAGONBORN, JUGGERNAUT);
        register(tempTraitToUnits, tempChampionMap, T_HEX, HEXMECH, PILTOVER, GUNSLINGER);
        register(tempTraitToUnits, tempChampionMap, TAHM_KENCH, GLUTTON, BILGEWATER, BRUISER);
        register(tempTraitToUnits, tempChampionMap, THRESH, SHADOW_ISLES, WARDEN);
        register(tempTraitToUnits, tempChampionMap, VOLIBEAR, FRELJORD, BRUISER);
        register(tempTraitToUnits, tempChampionMap, XERATH, ASCENDANT, SHURIMA);
        register(tempTraitToUnits, tempChampionMap, ZIGGS, YORDLE, ZAUN, LONGSHOT);
        register(tempTraitToUnits, tempChampionMap, ZILEAN, CHRONOKEEPER, INVOKER);

        // 7-Cost Champions
        register(tempTraitToUnits, tempChampionMap, AURELION_SOL, STAR_FORGER, TARGON);
        register(tempTraitToUnits, tempChampionMap, BARON_NASHOR, RIFTSCOURGE, VOID);
        register(tempTraitToUnits, tempChampionMap, BROCK, IXTAL);
        register(tempTraitToUnits, tempChampionMap, SYLAS, CHAINBREAKER, DEFENDER, ARCANIST);
        register(tempTraitToUnits, tempChampionMap, ZAAHEN, IMMORTAL, IONIA, DEMACIA, WARDEN);


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
        Unit unit = new Unit(champ.getDisplayName(), 0, Role.ADCASTER, new ChampionStats(), List.of(traits));
        championMap.put(champ, unit);
        for (Trait t : traits) {
            traitToUnits.computeIfAbsent(t, k -> new ArrayList<>()).add(unit);
        }
    }

    private void initPrefixTrie() {
        Arrays.stream(Champion.values()).forEach(this.championPrefixTrie::add);
        Arrays.stream(Trait.values()).forEach(this.traitPrefixTrie::add);
        Arrays.stream(Emblem.values()).forEach(this.emblemPrefixTrie::add);

    }

    /**
     * Gets all units grouped by trait
     * @return Map of all units grouped by trait
     */
    public List<Unit> getUnitsByTrait(Trait trait) {
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
    public List<Unit> getAllUnits() {
        return unitRepository.getAllUnits();
    }

    public Unit getUnitByName(String unitName) {
        return unitRepository.getUnitByName(unitName);
    }

    public Trait getTraitByName(String traitName) {
        return traitRepository.getTraitByName(traitName);
    }

    public List<Trait> getAllTraits() {
        return traitRepository.getAllTraits();
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

    public List<Emblem> getAllEmblemsStartingWith(String prefix)
    {
        return this.emblemPrefixTrie.getAllDescendantsByPrefix(prefix);
    }

    /**
     * Gets units according to filter params in FilterDTO
     * @param filterDTO Contains filter data {@link FilterDTO}
     * @return List of units {@link Unit}
     */
    public List<Unit> filter(FilterDTO filterDTO) {
        Set<Unit> filteredUnits = new HashSet<>(getAllUnits());

        if (!filterDTO.getUnits().isEmpty()) {
            // From unitDTOList map displayName -> actual unit
            List<Unit> unitList = filterDTO.getUnits().stream().map(unitDTO -> unitRepository.getUnitByName(unitDTO.getUnit())).toList();

            // For each unit in unitList map unit -> SingletonSet(Unit)
            List<Set<Unit>> unitsFromChampions = unitList.stream().map(Collections::singleton).toList();

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

    /**
     * @param horizontalDTO
     */
    public List<List<Unit>> getHorizontalComps(HorizontalDTO horizontalDTO) {
        List<Set<Unit>> comps = new ArrayList<>();

        Manager manager = new Manager(horizontalDTO, this, comps, this.getAllUnits());

        int numComps = horizontalDTO.getNumberOfComps();

        TFTEngine tftEngine = new TFTEngine(manager);

        return tftEngine.buildComps(numComps).stream().map(comp -> comp.stream().toList()).toList();
    }
}
