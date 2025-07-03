package com.tfttools.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enumerates champion traits
 */
public enum Trait implements Namable
{
    AMP("Amp"),
    ANIMA_SQUAD("Anima Squad"),
    BASTION("Bastion"),
    BOOMBOTS("Boombots"),
    BRUISER("Bruiser"),
    CYBERBOSS("Cyberboss"),
    CYPHER("Cypher"),
    DIVINICORP("Divinicorp"),
    DYNAMO("Dynamo"),
    EXECUTIONER("Executioner"),
    EXOTECH("Exotech"),
    GOD_OF_THE_NET("God Of The Net"),
    GOLDEN_OX("Golden Ox"),
    MARKSMAN("Marksman"),
    NITRO("Nitro"),
    OVERLORD("Overlord"),
    RAPIDFIRE("Rapidfire"),
    SLAYER("Slayer"),
    STRATEGIST("Strategist"),
    STREET_DEMON("Street Demon"),
    SYNDICATE("Syndicate"),
    TECHIE("Techie"),
    VANGUARD("Vanguard"),
    VIRUS("Virus"),
    SOUL_KILLER("Soul Killer");

    private final String displayName;

    Trait(String displayName) { this.displayName = displayName; }

    private static final Map<String, Trait> BY_DISPLAY_NAME = Arrays.stream(values()).collect(Collectors.toMap(Trait::getDisplayName, Function.identity()));

    public static Trait fromDisplayName(String displayName) {
        return BY_DISPLAY_NAME.get(displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
