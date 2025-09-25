package com.tfttools.registry;

import com.tfttools.domain.*;
import com.tfttools.domain.repository.TraitRepository;
import com.tfttools.domain.repository.UnitRepository;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.engine.TFTEngine;
import com.tfttools.engine.manager.Manager;
import com.tfttools.prefixtrie.PrefixTrie;
import com.tfttools.dto.FilterDTO;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Initialize Units and their respective traits
 */
@Component
public class UnitRegistry {
    private Map<Trait, List<Unit>> traitToUnits;
    private final PrefixTrie<Unit> unitPrefixTrie;
    private final PrefixTrie<Trait> traitPrefixTrie;
    private final TraitRepository traitRepository;
    private final UnitRepository unitRepository;

    public UnitRegistry() {
        this.traitToUnits = new HashMap<>();
        this.unitPrefixTrie = new PrefixTrie<>();
        this.traitPrefixTrie = new PrefixTrie<>();

        this.traitRepository = new TraitRepository();
        this.unitRepository = new UnitRepository(traitRepository);

        initRegistry();
    }

    public UnitRegistry(String set) {
        this.traitToUnits = new HashMap<>();
        this.unitPrefixTrie = new PrefixTrie<>();
        this.traitPrefixTrie = new PrefixTrie<>();

        this.traitRepository = new TraitRepository(set);
        this.unitRepository = new UnitRepository(traitRepository, set);

        initRegistry();
    }

    private void initRegistry() {
        //Initialize Prefix Tries
        initPrefixTrie();

        // Initialize traitToUnits
        traitRepository.getAllTraits().forEach(trait -> traitToUnits.put(trait, new ArrayList<>()));
        List<Unit> units = unitRepository.getAllUnits();
        units.forEach(unit -> unit.getTraits().forEach(trait -> traitToUnits.get(trait).add(unit)));

        // Make all trait lists unmodifiable
        for (Map.Entry<Trait, List<Unit>> entry : traitToUnits.entrySet()) {
            entry.setValue(Collections.unmodifiableList(entry.getValue()));
        }

        // Wrap outer maps as unmodifiable
        traitToUnits = Collections.unmodifiableMap(traitToUnits);
    }

    private void initPrefixTrie() {
        unitRepository.getAllUnits().forEach(this.unitPrefixTrie::add);
        traitRepository.getAllTraits().forEach(this.traitPrefixTrie::add);
    }

    /**
     * Gets all units grouped by trait
     *
     * @return List of all units grouped by trait
     */
    public List<Unit> getUnitsByTrait(Trait trait) {
        return traitToUnits.getOrDefault(trait, new ArrayList<>());
    }

    /**
     * Gets all units in the {@link UnitRegistry}
     *
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
     *
     * @param prefix The prefix to be searched for
     * @return List of champions
     */
    public List<Unit> getAllChampionsStartingWith(String prefix) {
        return this.unitPrefixTrie.getAllDescendantsByPrefix(prefix);
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
            List<Trait> traitList = filterDTO.getTraits().stream().map(traitDTO -> traitRepository.getTraitByName(traitDTO.getDisplayName())).toList();
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
