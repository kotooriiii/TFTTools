package com.tfttools.domain.communitydragon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Data
public class ChampionStats
{
    @JsonProperty(value = "armor")
    private int armor;

    @JsonProperty(value = "attack_speed")
    private int attackSpeed;

    @JsonProperty(value = "crit_chance")
    private int critChance;

    @JsonProperty(value = "crit_multiplier")
    private int critMultiplier;

    @JsonProperty(value = "damage")
    private int damage;

    @JsonProperty(value = "hp")
    private int hp;

    @JsonProperty(value = "initialMana")
    private int initialMana;

    @JsonProperty(value = "magicResist")
    private int magicResist;

    @JsonProperty(value = "mana")
    private int mana;

    @JsonProperty(value = "range")
    private int range;
}
