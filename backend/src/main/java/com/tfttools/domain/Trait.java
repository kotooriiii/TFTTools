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
    AMP("Amp", new int[]{2,3,4,5}),
    ANIMA_SQUAD("Anima Squad", new int[]{3,5,7,10}),
    BASTION("Bastion", new int[]{2,4,6}),
    BOOMBOT("Boombot", new int[]{2,4,6}),
    BRUISER("Bruiser", new int[]{2,4,6}),
    CYBERBOSS("Cyberboss", new int[]{2,3,4}),
    CYPHER("Cypher", new int[]{3,4,5}),
    DIVINICORP("Divinicorp", new int[]{1,2,3,4,5,6,7}),
    DYNAMO("Dynamo", new int[]{2,3,4}),
    EXECUTIONER("Executioner", new int[]{2,3,4,5}),
    EXOTECH("Exotech", new int[]{3,5,7,10}),
    GOD_OF_THE_NET("God Of The Net", new int[]{1}),
    GOLDEN_OX("Golden Ox", new int[]{2,4,6}),
    MARKSMAN("Marksman", new int[]{2,4}),
    NITRO("Nitro", new int[]{3,4}),
    OVERLORD("Overlord", new int[]{1}),
    RAPIDFIRE("Rapidfire", new int[]{2,4,6}),
    SLAYER("Slayer", new int[]{2,4,6}),
    STRATEGIST("Strategist", new int[]{2,3,4,5}),
    STREET_DEMON("Street Demon", new int[]{3,5,7,10}),
    SYNDICATE("Syndicate", new int[]{3,5,7}),
    TECHIE("Techie", new int[]{2,4,6,8}),
    VANGUARD("Vanguard", new int[]{2,4,6}),
    VIRUS("Virus", new int[]{1}),
    SOUL_KILLER("Soul Killer", new int[]{1});

    private final String displayName;
    private final int[] thresholds;

    Trait(String displayName, int[] thresholds) {
        this.displayName = displayName;
        this.thresholds = thresholds;
    }

    private static final Map<String, Trait> BY_DISPLAY_NAME = Arrays.stream(values()).collect(Collectors.toMap(Trait::getDisplayName, Function.identity()));

    public static Trait fromDisplayName(String displayName) {
        return BY_DISPLAY_NAME.get(displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

    public int[] getThresholds() {
        return thresholds;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
