package com.tfttools.domain;

/**
 * Enumerates champion traits
 */
public enum Trait
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

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
