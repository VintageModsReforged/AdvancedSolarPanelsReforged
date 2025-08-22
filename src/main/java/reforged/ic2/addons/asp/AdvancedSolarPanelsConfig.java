package reforged.ic2.addons.asp;

import mods.vintage.core.helpers.ConfigHelper;
import mods.vintage.core.platform.config.ItemBlockID;
import mods.vintage.core.platform.lang.LocalizationProvider;
import net.minecraftforge.common.Configuration;

@LocalizationProvider
public class AdvancedSolarPanelsConfig extends Configuration {

    public static final String SOLARS = "solar-energy-values";

    @LocalizationProvider.List(modId = References.MOD_ID)
    public static String[] LANGS;
    public static ItemBlockID ASP_META_BLOCK_ID = ItemBlockID.ofBlock("aspMetaBlockID", 194);
    public static ItemBlockID ASP_TRANSFORMER_ID = ItemBlockID.ofBlock("aspTransformer", 195);
    public static ItemBlockID ASP_ITEM_ID = ItemBlockID.ofItem("aspMetaItemID", 30575);
    public static ItemBlockID ADVANCED_HELMET_ID = ItemBlockID.ofItem("aspAdvancedHelmet", 30576);
    public static ItemBlockID HYBRID_HELMET_ID = ItemBlockID.ofItem("aspHybridHelmet", 30577);
    public static ItemBlockID ULTIMATE_HELMET_ID = ItemBlockID.ofItem("aspUltimateHelmet", 30578);

    // solar
    public static SolarConfig ADVANCED;
    public static SolarConfig HYBRID;
    public static SolarConfig ULTIMATE;
    public static SolarConfig QUANTUM;
    public static SolarConfig SPECTRAL;
    public static SolarConfig SINGULAR;
    public static SolarConfig LIGHT_ABSORBING;
    public static SolarConfig PHOTONIC;

    public static boolean HARD_RECIPES = true;
    public static boolean DOUBLE_SLAB = true;
    public static boolean ADVANCED_SOLAR = true;
    public static boolean HYBRID_SOLAR = true;
    public static boolean ULTIMATE_SOLAR = true;
    public static boolean QUANTUM_SOLAR = true;
    public static boolean SPECTRAL_SOLAR = true;
    public static boolean SINGULAR_SOLAR = true;
    public static boolean LIGHT_ABSORBING_SOLAR = true;
    public static boolean PHOTONIC_SOLAR = true;
    public static boolean ADVANCED_HELMET = true;
    public static boolean HYBRID_HELMET = true;
    public static boolean ULTIMATE_HELMET = true;

    public AdvancedSolarPanelsConfig() {
        super(ConfigHelper.getConfigFileFor(References.MOD_ID + "/main"));
        load();
        // lang
        LANGS = ConfigHelper.getLocalizations(this, new String[] {"en_US"}, References.MOD_ID);
        // solar energy vales
        ADVANCED = new SolarConfig(this, "advanced", 8, 1, 32000, 1);
        HYBRID = new SolarConfig(this, "hybrid", 64, 8, 100000, 2);
        ULTIMATE = new SolarConfig(this, "ultimate", 512, 64, 1000000, 3);
        QUANTUM = new SolarConfig(this, "quantum", 4096, 2048, 10000000, 4);

        SPECTRAL = new SolarConfig(this, "spectral", 8192, 4096, 100000000, 5);
        SINGULAR = new SolarConfig(this, "singular", 32768, 16384, 1000000000, 6);
        LIGHT_ABSORBING = new SolarConfig(this, "light_absorbing", 131072, 65536, 1000000000, 7);
        PHOTONIC = new SolarConfig(this, "photonic", 524288, 262144, 1000000000, 8);

        // recipes
        HARD_RECIPES = ConfigHelper.getBoolean(this, "recipes-general", "hardRecipes", HARD_RECIPES, "Enable Hard Recipes");
        DOUBLE_SLAB = ConfigHelper.getBoolean(this, "recipes-panels", "doubleSlab", DOUBLE_SLAB, "Enable Double Stone Slab Recipe");
        ADVANCED_SOLAR = ConfigHelper.getBoolean(this, "recipes-panels", "advanced", ADVANCED_SOLAR, "Enable Advanced Solar Panel Recipe");
        HYBRID_SOLAR = ConfigHelper.getBoolean(this, "recipes-panels", "hybrid", HYBRID_SOLAR, "Enable Hybrid Solar Panel Recipe");
        ULTIMATE_SOLAR = ConfigHelper.getBoolean(this, "recipes-panels", "ultimate", ULTIMATE_SOLAR, "Enable Ultimate Solar Panel Recipe");
        QUANTUM_SOLAR = ConfigHelper.getBoolean(this, "recipes-panels", "quantum", QUANTUM_SOLAR, "Enable Quantum Solar Panel Recipe");
        SPECTRAL_SOLAR = ConfigHelper.getBoolean(this, "recipes-panels", "spectral", SPECTRAL_SOLAR, "Enable Spectral Solar Panel Recipe");
        SINGULAR_SOLAR = ConfigHelper.getBoolean(this, "recipes-panels", "singular", SINGULAR_SOLAR, "Enable Singular Solar Panel Recipe");
        LIGHT_ABSORBING_SOLAR = ConfigHelper.getBoolean(this, "recipes-panels", "light_absorbing", LIGHT_ABSORBING_SOLAR, "Enable Light Absorbing Solar Panel Recipe");
        PHOTONIC_SOLAR = ConfigHelper.getBoolean(this, "recipes-panels", "photonic", PHOTONIC_SOLAR, "Enable Photonic Solar Panel Recipe");
        ADVANCED_HELMET = ConfigHelper.getBoolean(this, "recipes-helmet", "advanced", ADVANCED_HELMET, "Enable Advanced Solar Helmet Recipe");
        HYBRID_HELMET = ConfigHelper.getBoolean(this, "recipes-helmet", "hybrid", HYBRID_HELMET, "Enable Hybrid Solar Helmet Recipe");
        ULTIMATE_HELMET = ConfigHelper.getBoolean(this, "recipes-helmet", "ultimate", ULTIMATE_HELMET, "Enable Ultimate Solar Helmet Recipe");
    }

    public static class SolarConfig {

        public final int[] ENERGY_VALUES;

        public SolarConfig(Configuration handler, String prefix, int defDay, int defNight, int defStorage, int defTier) {
            int[] defaults = new int[] { defDay, defNight, defStorage, defTier };
            ENERGY_VALUES = ConfigHelper.getInts(handler, SOLARS, prefix, defaults, "Values: genDay, genNight, storage, tier");
        }

        public int getGenDay() {
            return this.ENERGY_VALUES[0];
        }

        public int getGenNight() {
            return this.ENERGY_VALUES[1];
        }

        public int getStorage() {
            return this.ENERGY_VALUES[2];
        }

        public int getTier() {
            return this.ENERGY_VALUES[3];
        }
    }
}
