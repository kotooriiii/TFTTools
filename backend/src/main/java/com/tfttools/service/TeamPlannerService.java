package com.tfttools.service;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Unit;
import com.tfttools.domain.communitydragon.TeamPlannerChampion;
import com.tfttools.domain.communitydragon.TeamPlannerData;
import com.tfttools.repository.UnitRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@DependsOn({"TFTSetContextService", "unitRepository"})
public class TeamPlannerService
{

    private final CommunityDragonDataService dataService;
    private final TFTSetContextService setContextService;
    private final UnitRepository unitRepository;

    private Map<String, String> championNameToCodeMap; // champion apiName -> code

    @Autowired
    public TeamPlannerService(CommunityDragonDataService dataService, TFTSetContextService setContextService, UnitRepository unitRepository)
    {
        this.dataService = dataService;
        this.setContextService = setContextService;
        this.unitRepository = unitRepository;
    }

    @PostConstruct
    public void init()
    {
        buildTeamPlannerCodeMap();
    }

    private void buildTeamPlannerCodeMap()
    {
        championNameToCodeMap = new HashMap<>();
        TeamPlannerData data = dataService.getTeamPlannerData();

        if (data != null)
        {
            String currentSetKey = setContextService.getCurrentSetKey();
            if (currentSetKey != null)
            {
                List<TeamPlannerChampion> champions = data.getSetData(currentSetKey);
                if (champions != null)
                {
                    for (TeamPlannerChampion champion : champions)
                    {
                        championNameToCodeMap.put(champion.getCharacterId(), String.format("%03x", champion.getTeamPlannerCode()));
                    }
                }
            }
        }

        addFallbackCodesForVariantUnits();
    }

    /**
     * The team planner feed only lists one entry per base champion. Trait-variant champions
     * (e.g. "Lux (Inferno)") have no entry of their own in that feed - in the game client,
     * Team Planner has no concept of variants and represents them all with the base
     * champion's code, so we mirror that here using UnitRepository's variant/base linkage.
     */
    private void addFallbackCodesForVariantUnits()
    {
        for (Unit unit : unitRepository.getAllUnits())
        {
            if (championNameToCodeMap.containsKey(unit.getApiName()))
            {
                continue;
            }

            unitRepository.getBaseUnit(unit)
                    .map(baseUnit -> championNameToCodeMap.get(baseUnit.getApiName()))
                    .ifPresent(baseCode -> championNameToCodeMap.put(unit.getApiName(), baseCode));
        }
    }

    /**
     * Converts a Unit to its hex representation using team planner codes
     *
     * @param unit The unit to convert
     * @return Hex string representation of the unit's team planner code
     */
    public String unitToHex(Unit unit)
    {
        String hexCode = championNameToCodeMap.get(unit.getApiName());

        if (hexCode == null)
        {
            throw new IllegalArgumentException("No team planner code found for unit: " + unit.getDisplayName());

        }

        return hexCode;
    }

    public String exportToTeamCode(Composition composition)
    {
        return exportToTeamCode(composition.getUnits());
    }

    /**
     * Lighter-weight path for callers that only have an ordered list of units (not a full
     * engine-generated {@link Composition}) - e.g. a saved My Comps entry or the current Team
     * Builder board.
     */
    public String exportToTeamCode(List<Unit> units)
    {
        StringBuilder sb = new StringBuilder("02");

        //Can only place 10 units into teamCode
        for (int i = 0; i < 10; i++)
        {
            if (i < units.size())
            {
                sb.append(unitToHex(units.get(i)));
            } else
            {
                sb.append("000");
            }

        }

        sb.append(setContextService.getCurrentSetKey());
        return sb.toString();
    }

    public void refreshTeamPlannerCodes()
    {
        buildTeamPlannerCodeMap();
    }

}