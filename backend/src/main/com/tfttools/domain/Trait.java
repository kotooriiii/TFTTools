package main.com.tfttools.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enumerates traits for TFT Set 16: Lore & Legends
 */
public enum Trait implements Namable {
    // Origins
    ASCENDANT("Ascendant", new int[]{1}),
    ASSIMILATOR("Assimilator", new int[]{1}),
    BILGEWATER("Bilgewater", new int[]{3, 5, 7, 10}),
    BLACKSMITH("Blacksmith", new int[]{1}),
    CARETAKER("Caretaker", new int[]{1}),
    CHAINBREAKER("Chainbreaker", new int[]{1}),
    CHRONOKEEPER("Chronokeeper", new int[]{1}),
    DARK_CHILD("Dark Child", new int[]{1}),
    DARKIN("Darkin", new int[]{1,2,3}),
    DEMACIA("Demacia", new int[]{3, 5, 7, 10}),
    DRAGONBORN("Dragonborn", new int[]{1}),
    EMPEROR("Emperor", new int[]{1}),
    ETERNAL("Eternal", new int[]{1}),
    FRELJORD("Freljord", new int[]{3, 5, 7}),
    GLUTTON("Glutton", new int[]{1}),
    HARVESTER("Harvester", new int[]{1}),
    HEROIC("Heroic", new int[]{1}),
    HEXMECH("HexMech", new int[]{1}),
    HUNTRESS("Huntress", new int[]{1}),
    IMMORTAL("Immortal", new int[]{1}),
    IONIA("Ionia", new int[]{3, 5, 7, 10}),
    IXTAL("Ixtal", new int[]{3,5,7}),
    NOXUS("Noxus", new int[]{3, 5, 7, 10}),
    PILTOVER("Piltover", new int[]{2, 4, 6}),
    RIFTSCOURGE("Riftscourge", new int[]{1}),
    RUNE_MAGE("Rune Mage", new int[]{1}),
    SHADOW_ISLES("Shadow Isles", new int[]{2,3,4,5}),
    SHURIMA("Shurima", new int[]{2,3,4}),
    SOULBOUND("Soulbound", new int[]{1}),
    STAR_FORGER("Star Forger", new int[]{1}),
    TARGON("Targon", new int[]{1}),
    THE_BOSS("The Boss", new int[]{1}),
    VOID("Void", new int[]{2,4,6,9}),
    WORLD_ENDER("World Ender", new int[]{1}),
    YORDLE("Yordle", new int[]{2,4,6,8,10}),
    ZAUN("Zaun", new int[]{3, 5, 7}),
    
    // Classes
    ARCANIST("Arcanist", new int[]{2, 4, 6}),
    BRUISER("Bruiser", new int[]{2, 4, 6}),
    DEFENDER("Defender", new int[]{2, 4, 6}),
    DISRUPTOR("Disruptor", new int[]{2, 4}),
    GUNSLINGER("Gunslinger", new int[]{2, 4}),
    INVOKER("Invoker", new int[]{2, 4}),
    JUGGERNAUT("Juggernaut", new int[]{2, 4, 6}),
    LONGSHOT("Longshot", new int[]{2,3,4,5}),
    QUICKSTRIKER("Quickstriker", new int[]{2,3,4,5}),
    SLAYER("Slayer", new int[]{2, 4, 6}),
    VANQUISHER("Vanquisher", new int[]{2,3,4,5}),
    WARDEN("Warden", new int[]{2,3,4,5});

    private final String displayName;
    private final int[] activationThresholds;


    Trait(String displayName, int[] activationThresholds)
    {
        this.displayName = displayName;
        this.activationThresholds = activationThresholds;
    }

    private static final Map<String, Trait> BY_DISPLAY_NAME = Arrays.stream(values())
        .collect(Collectors.toMap(Trait::getDisplayName, Function.identity()));

    public static Trait fromDisplayName(String displayName) {
        return BY_DISPLAY_NAME.get(displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

    public int[] getActivationThresholds()
    {
        return activationThresholds;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}