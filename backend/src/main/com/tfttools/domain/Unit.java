package main.com.tfttools.domain;

import com.tfttools.domain.repository.communitydragon.ChampionStats;

import java.util.List;

/**
 * Abstraction of a unit in Team Fight Tactics
 */
public class Unit implements Namable{
    private String name;
    private int cost;
    private Role role;
    private ChampionStats championStats;
    private List<Trait> traits;

    public Unit(String name, int cost, Role role, ChampionStats championStats, List<Trait> traits) {
        this.name = name;
        this.cost = cost;
        this.role = role;
        this.championStats = championStats;
        this.traits = traits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ChampionStats getChampionStats() {
        return championStats;
    }

    public void setChampionStats(ChampionStats championStats) {
        this.championStats = championStats;
    }

    public List<Trait> getTraits() {
        return traits;
    }

    public void setTraits(List<Trait> traits) {
        this.traits = traits;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
