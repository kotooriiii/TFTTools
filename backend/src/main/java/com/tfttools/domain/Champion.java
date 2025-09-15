package com.tfttools.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enumerates champion names
 */
public enum Champion implements Namable{
    BRAUM("Braum"),
    GWEN("Gwen"),
    LEE_SIN("Lee Sin"),
    SERAPHINE("Seraphine"),
    TWISTED_FATE("Twisted Fate"),
    VARUS("Varus"),
    YONE("Yone"),
    ZYRA("Zyra"),
    AKALI("Akali"),
    ASHE("Ashe"),
    JARVAN_IV("Jarvan IV"),
    JINX("Jinx"),
    KSANTE("K'Sante"),
    KARMA("Karma"),
    LEONA("Leona"),
    POPPY("Poppy"),
    RYZE("Ryze"),
    SAMIRA("Samira"),
    SETT("Sett"),
    VOLIBEAR("Volibear"),
    YUUMI("Yuumi"),
    AHRI("Ahri"),
    CAITLYN("Caitlyn"),
    DARIUS("Darius"),
    JAYCE("Jayce"),
    KOGMAW("Kog'Maw"),
    LULU("Lulu"),
    MALZAHAR("Malzahar"),
    NEEKO("Neeko"),
    RAMMUS("Rammus"),
    SENNA("Senna"),
    SMOLDER("Smolder"),
    SWAIN("Swain"),
    UDYR("Udyr"),
    VIEGO("Viego"),
    YASUO("Yasuo"),
    ZIGGS("Ziggs"),
    DR_MUNDO("Dr. Mundo"),
    GANGPLANK("Gangplank"),
    JANNA("Janna"),
    JHIN("Jhin"),
    KAISA("Kai'Sa"),
    KATARINA("Katarina"),
    KOBUKO("Kobuko"),
    LUX("Lux"),
    RAKAN("Rakan"),
    SHEN("Shen"),
    VI("Vi"),
    XAYAH("Xayah"),
    XIN_ZHAO("Xin Zhao"),
    AATROX("Aatrox"),
    EZREAL("Ezreal"),
    GAREN("Garen"),
    GNAR("Gnar"),
    KALISTA("Kalista"),
    KAYLE("Kayle"),
    KENNEN("Kennen"),
    LUCIAN("Lucian"),
    MALPHITE("Malphite"),
    NAAFIRI("Naafiri"),
    RELL("Rell"),
    SIVIR("Sivir"),
    SYNDRA("Syndra"),
    ZAC("Zac");
//    ALISTAR("Alistar"),
//    ANNIE("Annie"),
//    APHELIOS("Aphelios"),
//    AURORA("Aurora"),
//    BRAND("Brand"),
//    BRAUM("Braum"),
//    CHO_GATH("Cho'Gath"),
//    DARIUS("Darius"),
//    DR_MUNDO("Dr. Mundo"),
//    DRAVEN("Draven"),
//    EKKO("Ekko"),
//    ELISE("Elise"),
//    FIDDLESTICKS("Fiddlesticks"),
//    GALIO("Galio"),
//    GAREN("Garen"),
//    GRAGAS("Gragas"),
//    GRAVES("Graves"),
//    ILLAOI("Illaoi"),
//    JARVAN_IV("Jarvan IV"),
//    JAX("Jax"),
//    JHIN("Jhin"),
//    JINX("Jinx"),
//    KINDRED("Kindred"),
//    KOBUKO("Kobuko"),
//    KOGMAW("Kogmaw"),
//    LEBLANC("Leblanc"),
//    LEONA("Leona"),
//    MISS_FORTUNE("Miss Fortune"),
//    MORDEKAISER("Mordekaiser"),
//    MORGANA("Morgana"),
//    NAAFIRI("Naafiri"),
//    NEEKO("Neeko"),
//    NIDALEE("Nidalee"),
//    POPPY("Poppy"),
//    RENEKTON("Renekton"),
//    RENGAR("Rengar"),
//    RHAAST("Rhaast"),
//    SAMIRA("Samira"),
//    SEJUANI("Sejuani"),
//    SENNA("Senna"),
//    SERAPHINE("Seraphine"),
//    SHACO("Shaco"),
//    SHYVANA("Shyvana"),
//    SKARNER("Skarner"),
//    SYLAS("Sylas"),
//    TWISTED_FATE("Twisted Fate"),
//    URGOT("Urgot"),
//    VARUS("Varus"),
//    VAYNE("Vayne"),
//    VEIGAR("Veigar"),
//    VEX("Vex"),
//    VI("Vi"),
//    VIEGO("Viego"),
//    XAYAH("Xayah"),
//    YUUMI("Yuumi"),
//    ZAC("Zac"),
//    ZED("Zed"),
//    ZERI("Zeri"),
//    ZIGGS("Ziggs"),
//    ZYRA("Zyra");

    private final String displayName;

    Champion(String name) {
        this.displayName = name;
    }

    private static final Map<String, Champion> BY_DISPLAY_NAME = Arrays.stream(values()).collect(Collectors.toMap(Champion::getDisplayName, Function.identity()));

    public static Champion fromDisplayName(String displayName) {
        return BY_DISPLAY_NAME.get(displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
