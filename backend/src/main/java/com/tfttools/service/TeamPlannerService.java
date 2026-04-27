package com.tfttools.service;

import com.tfttools.domain.Composition;
import com.tfttools.domain.Unit;
import com.tfttools.domain.communitydragon.TeamPlannerChampion;
import com.tfttools.domain.communitydragon.TeamPlannerData;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@DependsOn("TFTSetContextService")
public class TeamPlannerService
{

    private final CommunityDragonDataService dataService;
    private final TFTSetContextService setContextService;

    private Map<String, String> championNameToCodeMap; // champion name -> code

    @Autowired
    public TeamPlannerService(CommunityDragonDataService dataService, TFTSetContextService setContextService)
    {
        this.dataService = dataService;
        this.setContextService = setContextService;
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
        List<Unit> units = composition.getUnits();

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