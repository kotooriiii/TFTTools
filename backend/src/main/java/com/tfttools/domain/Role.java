package com.tfttools.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Role {
    APCASTER("APCaster"),
    ADCASTER("ADCaster"),
    APTANK("APTank"),
    ADTANK("ADTank"),
    APFIGHTER("APFighter"),
    ADFIGHTER("ADFighter"),
    APSPECIALIST("APSpecialist"),
    ADSPECIALIST("ADSpecialist"),
    ADCARRY("ADCarry"),
    APCARRY("APCarry");

    private final String displayName;

    Role(String role) {
        this.displayName = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static final Map<String, Role> BY_DISPLAY_NAME = Arrays.stream(values()).collect(Collectors.toMap(Role::getDisplayName, Function.identity()));

    public static Role getRoleFromDisplayName(String displayName) {
        return BY_DISPLAY_NAME.get(displayName);
    }
}
