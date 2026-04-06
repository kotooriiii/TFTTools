package com.tfttools.domain.repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfttools.domain.Role;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.domain.repository.communitydragon.ChampionStats;
import com.tfttools.domain.repository.communitydragon.CommunityDragonChampions;
import com.tfttools.domain.repository.communitydragon.CommunityDragonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://raw.communitydragon.org/latest/cdragon/tft/en_us.json
 */

@Component
public class UnitRepository
{
    private final Map<String, Unit> units;
    private final TraitRepository traitRepository;

    @Autowired
    public UnitRepository(TraitRepository traitRepository)
    {
        this.units = new HashMap<>();
        this.traitRepository = traitRepository;

        parseJson("16");
    }

    public UnitRepository(TraitRepository traitRepository, String set)
    {
        this.units = new HashMap<>();
        this.traitRepository = traitRepository;

        parseJson(set);
    }

    private void parseJson(String set)
    {
        try
        {
            InputStream inputStream = getClass().getResourceAsStream("/com/tfttools/domain/repository/communitydragon/en_us.json"); //todo dupe
            if (inputStream == null)
            {
                throw new FileNotFoundException("Resource not found");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            CommunityDragonObject communityDragonObject = objectMapper.readValue(inputStream, CommunityDragonObject.class);

            List<CommunityDragonChampions> units = communityDragonObject.getSets().get(set).getChampions();
            for (CommunityDragonChampions champions : units)
            {
                String name = champions.getName().trim();
                int cost = champions.getCost();
                Role role = Role.getRoleFromDisplayName(champions.getRole());
                ChampionStats championStats = champions.getStats();
                List<Trait> traits = champions.getTraits().stream().map(traitRepository::getTraitByName).toList();

                if (traits.isEmpty())
                {
                    continue;
                }

                this.units.put(name, new Unit(name, cost, role, championStats, traits));
            }
        } catch (Exception e)
        {
            e.printStackTrace(); //todo, need smthn like log4j
        }
    }

    public Unit getUnitByName(String unit)
    {
        return this.units.get(unit);
    }

    public List<Unit> getAllUnits()
    {
        return new ArrayList<>(this.units.values());
    }
}
