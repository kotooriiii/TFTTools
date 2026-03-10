package com.tfttools.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Emblem implements Namable
{
    BILGEWATER("Bilgewater"),
    DEMACIA("Demacia"),
    FRELJORD("Freljord"),
    IONIA("Ionia"),
    IXTAL("Ixtal"),
    NOXUS("Noxus"),
    PILTOVER("Piltover"),
    VOID("Void"),
    YORDLE("Yordle"),
    ZAUN("Zaun"),

    ARCANIST("Arcanist"),
    BRUISER("Bruiser"),
    DEFENDER("Defender"),
    DISRUPTOR("Disruptor"),
    GUNSLINGER("Gunslinger"),
    INVOKER("Invoker"),
    JUGGERNAUT("Juggernaut"),
    LONGSHOT("Longshot"),
    QUICKSTRIKER("Quickstriker"),
    SLAYER("Slayer"),
    VANQUISHER("Vanquisher"),
    WARDEN("Warden");

    private final String displayName;

    Emblem(String displayName) { this.displayName = displayName; }

    private static final Map<String, Emblem> BY_DISPLAY_NAME = Arrays.stream(values()).collect(Collectors.toMap(Emblem::getDisplayName, Function.identity()));

    public static Emblem fromDisplayName(String displayName) {
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