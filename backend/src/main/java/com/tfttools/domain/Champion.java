package com.tfttools.domain;

/**
 * Enumerates champion names
 */
public enum Champion {
    ALISTAR("Alistar"),
    ANNIE("Annie"),
    APHELIOS("Aphelios"),
    AURORA("Aurora"),
    BRAND("Brand"),
    BRAUM("Braum"),
    CHO_GATH("Cho'Gath"),
    DARIUS("Darius"),
    DR_MUNDO("Dr. Mundo"),
    DRAVEN("Draven"),
    EKKO("Ekko"),
    ELISE("Elise"),
    FIDDLESTICKS("Fiddlesticks"),
    GALIO("Galio"),
    GAREN("Garen"),
    GRAGAS("Gragas"),
    GRAVES("Graves"),
    ILLAOI("Illaoi"),
    JARVAN_IV("Jarvan IV"),
    JAX("Jax"),
    JHIN("Jhin"),
    JINX("Jinx"),
    KINDRED("Kindred"),
    KOBUKO("Kobuko"),
    KOGMAW("Kogmaw"),
    LEBLANC("Leblanc"),
    LEONA("Leona"),
    MISS_FORTUNE("Miss Fortune"),
    MORDEKAISER("Mordekaiser"),
    MORGANA("Morgana"),
    NAAFIRI("Naafiri"),
    NEEKO("Neeko"),
    NIDALEE("Nidalee"),
    POPPY("Poppy"),
    RENEKTON("Renekton"),
    RENGAR("Rengar"),
    RHAAST("Rhaast"),
    SAMIRA("Samira"),
    SEJUANI("Sejuani"),
    SENNA("Senna"),
    SERAPHINE("Seraphine"),
    SHACO("Shaco"),
    SHYVANA("Shyvana"),
    SKARNER("Skarner"),
    SYLAS("Sylas"),
    TWISTED_FATE("Twisted Fate"),
    URGOT("Urgot"),
    VARUS("Varus"),
    VAYNE("Vayne"),
    VEIGAR("Veigar"),
    VEX("Vex"),
    VI("Vi"),
    VIEGO("Viego"),
    XAYAH("Xayah"),
    YUUMI("Yuumi"),
    ZAC("Zac"),
    ZED("Zed"),
    ZERI("Zeri"),
    ZIGGS("Ziggs"),
    ZYRA("Zyra");

    private final String displayName;

    Champion(String name) {
        this.displayName = name;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
