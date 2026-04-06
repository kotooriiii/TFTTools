package com.tfttools.registry;

import com.tfttools.domain.*;
import com.tfttools.domain.Trait;
import com.tfttools.domain.repository.EmblemRepository;
import com.tfttools.domain.repository.TraitRepository;
import com.tfttools.domain.repository.UnitRepository;
import com.tfttools.prefixtrie.PrefixTrie;
import com.tfttools.dto.FilterDTO;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * Initialize Units and their respective traits
 */
@Component
public class UnitRegistry {
    private final Map<Trait, List<Unit>> traitToUnits;

    private final PrefixTrie<Unit> unitPrefixTrie;
    private final PrefixTrie<Trait> traitPrefixTrie;
    private final PrefixTrie<Emblem> emblemPrefixTrie;

    private final TraitRepository traitRepository;
    private final UnitRepository unitRepository;
    private final EmblemRepository emblemRepository;


    public UnitRegistry(TraitRepository traitRepository, UnitRepository unitRepository, EmblemRepository emblemRepository) {
        this.unitPrefixTrie = new PrefixTrie<>();
        this.traitPrefixTrie = new PrefixTrie<>();
        this.emblemPrefixTrie = new PrefixTrie<>();

        this.traitRepository = traitRepository;
        this.unitRepository = unitRepository;
        this.emblemRepository = emblemRepository;

        Map<Trait, List<Unit>> tempTraitToUnits = new HashMap<>();


        this.unitRepository.getAllUnits().forEach(unit -> {
            register(tempTraitToUnits, unit);
        });


        // Initialize Prefix Tries
        initPrefixTrie();

        // Make all trait lists unmodifiable
        for (Map.Entry<Trait, List<Unit>> entry : tempTraitToUnits.entrySet()) {
            entry.setValue(Collections.unmodifiableList(entry.getValue()));
        }

        // Wrap outer maps as unmodifiable
        traitToUnits = Collections.unmodifiableMap(tempTraitToUnits);
    }


    private void register(Map<Trait, List<Unit>> traitToUnits,  Unit unit) {
        for (Trait t : unit.getTraits()) {
            traitToUnits.computeIfAbsent(t, k -> new ArrayList<>()).add(unit);
        }
    }

    private void initPrefixTrie() {
        unitRepository.getAllUnits().forEach(this.unitPrefixTrie::add);
        traitRepository.getAllTraits().forEach(this.traitPrefixTrie::add);
        emblemRepository.getAllEmblems().forEach(this.emblemPrefixTrie::add);
    }

    /**
     * Gets all units grouped by trait
     * @return Map of all units grouped by trait
     */
    public List<Unit> getUnitsByTrait(Trait trait) {
        return traitToUnits.getOrDefault(trait, new ArrayList<>());
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

    public List<Trait> getAllTraits() {
        return traitRepository.getAllTraits();
    }

    public Trait getTraitByName(String traitName) {
        return traitRepository.getTraitByName(traitName);
    }

    public List<Emblem> getAllEmblems()
    {
        return emblemRepository.getAllEmblems();
    }

    public Emblem getEmblemByName(String emblemName)
    {
        return emblemRepository.getEmblemByName(emblemName);
    }



    /**
     * Gets all champions starting with a given prefix
     * @param prefix The prefix to be searched for
     * @return List of champions
     */
    public List<Unit> getAllChampionsStartingWith(String prefix) {
        return this.unitPrefixTrie.getAllDescendantsByPrefix(prefix);
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
            List<Unit> unitList = filterDTO.getUnits().stream().map(unitDTO -> unitRepository.getUnitByName(unitDTO.getDisplayName())).toList();

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


}
