package main.com.tfttools.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enumerates champion names for TFT Set 16: Lore & Legends
 */
public enum Champion implements Namable{
    AATROX("Aatrox"),
    AHRI("Ahri"),
    AMBESSA("Ambessa"),
    ANIVIA("Anivia"),
    ANNIE("Annie"),
    APHELIOS("Aphelios"),
    ASHE("Ashe"),
    AURELION_SOL("Aurelion Sol"),
    AZIR("Azir"),
    BARD("Bard"),
    BARON_NASHOR("Baron Nashor"),
    BEL_VETH("Bel'Veth"),
    BLITZCRANK("Blitzcrank"),
    BRAUM("Braum"),
    BRIAR("Briar"),
    BROCK("Brock"),
    CAITLYN("Caitlyn"),
    CHO_GATH("Cho'Gath"),
    DARIUS("Darius"),
    DIANA("Diana"),
    DR_MUNDO("Dr. Mundo"),
    DRAVEN("Draven"),
    EKKO("Ekko"),
    FIDDLESTICKS("Fiddlesticks"),
    FIZZ("Fizz"),
    GALIO("Galio"),
    GANGPLANK("Gangplank"),
    GAREN("Garen"),
    GRAVES("Graves"),
    GWEN("Gwen"),
    ILLAOI("Illaoi"),
    JARVAN_IV("Jarvan IV"),
    JHIN("Jhin"),
    JINX("Jinx"),
    KAI_SA("Kai'Sa"),
    KALISTA("Kalista"),
    KENNEN("Kennen"),
    KINDRED("Kindred"),
    KOBUKO_AND_YUUMI("Kobuko & Yuumi"),
    KOG_MAW("Kog'Maw"),
    LEBLANC("LeBlanc"),
    LEONA("Leona"),
    LISSANDRA("Lissandra"),
    LORIS("Loris"),
    LUCIAN_AND_SENNA("Lucian & Senna"),
    LULU("Lulu"),
    LUX("Lux"),
    MALZAHAR("Malzahar"),
    MEL("Mel"),
    MILIO("Milio"),
    MISS_FORTUNE("Miss Fortune"),
    NASUS("Nasus"),
    NAUTILUS("Nautilus"),
    NEEKO("Neeko"),
    NIDALEE("Nidalee"),
    ORIANNA("Orianna"),
    ORNN("Ornn"),
    POPPY("Poppy"),
    QIYANA("Qiyana"),
    REK_SAI("Rek'Sai"),
    RENEKTON("Renekton"),
    RIFT_HERALD("Rift Herald"),
    RUMBLE("Rumble"),
    RYZE("Ryze"),
    SEJUANI("Sejuani"),
    SERAPHINE("Seraphine"),
    SETT("Sett"),
    SHEN("Shen"),
    SHYVANA("Shyvana"),
    SINGED("Singed"),
    SION("Sion"),
    SKARNER("Skarner"),
    SONA("Sona"),
    SWAIN("Swain"),
    SYLAS("Sylas"),
    T_HEX("T-Hex"),
    TAHM_KENCH("Tahm Kench"),
    TARIC("Taric"),
    TEEMO("Teemo"),
    THRESH("Thresh"),
    TRISTANA("Tristana"),
    TRYNDAMERE("Tryndamere"),
    TWISTED_FATE("Twisted Fate"),
    VAYNE("Vayne"),
    VEIGAR("Veigar"),
    VI("Vi"),
    VIEGO("Viego"),
    VOLIBEAR("Volibear"),
    WARWICK("Warwick"),
    WUKONG("Wukong"),
    XERATH("Xerath"),
    XIN_ZHAO("Xin Zhao"),
    YASUO("Yasuo"),
    YONE("Yone"),
    YORICK("Yorick"),
    YUNARA("Yunara"),
    ZAAHEN("Zaahen"),
    ZIGGS("Ziggs"),
    ZILEAN("Zilean"),
    ZOE("Zoe");

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