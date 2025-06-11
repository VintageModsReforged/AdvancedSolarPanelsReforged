package reforged.ic2.addons.asp;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import reforged.ic2.addons.asp.blocks.BlockAdvancedSolarPanelsMeta;
import reforged.ic2.addons.asp.blocks.BlockMolecularTransformer;
import reforged.ic2.addons.asp.items.ItemArmorAdvancedSolarHelmet;
import reforged.ic2.addons.asp.items.ItemBlockAdvancedSolarPanelsMeta;
import reforged.ic2.addons.asp.items.ItemComponent;
import reforged.ic2.addons.asp.items.ItemMolecularTransformer;
import reforged.ic2.addons.asp.tiles.*;

public class ASPBlocksItems {

    public static Block ASP_META_BLOCK;
    public static Block ASP_TRANSFORMER;

    public static Item COMPONENT;

    public static Item ADVANCED_HELMET;
    public static Item HYBRID_HELMET;
    public static Item ULTIMATE_HELMET;

    public static void init() {
        COMPONENT = registerItem(new ItemComponent(), "component");
        ADVANCED_HELMET = registerItem(new ItemArmorAdvancedSolarHelmet(AdvancedSolarPanelsConfig.ADVANCED_HELMET.get(), "advanced", AdvancedSolarPanelsConfig.ADVANCED), "advanced_helmet");
        HYBRID_HELMET = registerItem(new ItemArmorAdvancedSolarHelmet(AdvancedSolarPanelsConfig.HYBRID_HELMET.get(), "hybrid", AdvancedSolarPanelsConfig.HYBRID), "hybrid_helmet");
        ULTIMATE_HELMET = registerItem(new ItemArmorAdvancedSolarHelmet(AdvancedSolarPanelsConfig.ULTIMATE_HELMET.get(), "ultimate", AdvancedSolarPanelsConfig.ULTIMATE), "ultimate_helmet");

        ASP_META_BLOCK = new BlockAdvancedSolarPanelsMeta();
        GameRegistry.registerBlock(ASP_META_BLOCK, ItemBlockAdvancedSolarPanelsMeta.class, "blockAdvSolarMeta");

        ASP_TRANSFORMER = new BlockMolecularTransformer();
        GameRegistry.registerBlock(ASP_TRANSFORMER, ItemMolecularTransformer.class, "blockMolecularTransformer");

        GameRegistry.registerTileEntity(SolarPanels.Advanced.class, "Advanced Solar Panel");
        GameRegistry.registerTileEntity(SolarPanels.Hybrid.class, "Hybrid Solar Panel");
        GameRegistry.registerTileEntity(SolarPanels.Ultimate.class, "Ultimate Hybrid Solar Panel");
        GameRegistry.registerTileEntity(SolarPanels.Quantum.class, "Quantum Solar Panel");
        GameRegistry.registerTileEntity(SolarPanels.Spectral.class, "Spectral Solar Panel");
        GameRegistry.registerTileEntity(SolarPanels.Singular.class, "Singular Solar Panel");
        GameRegistry.registerTileEntity(SolarPanels.LightAbsorbing.class, "Light Absorbing Solar Panel");
        GameRegistry.registerTileEntity(SolarPanels.Photonic.class, "Photonic Solar Panel");

        GameRegistry.registerTileEntity(TileEntityQuantumGenerator.class, "Quantum Generator");
        GameRegistry.registerTileEntity(TileEntityMolecularTransformer.class, "Molecular Transformer");
    }

    public static  <T extends Item> T registerItem(T item, String regName) {
        GameRegistry.registerItem(item, regName);
        return item;
    }

    public enum Component {
        // parts
        SUNNARIUM_PART,
        IRRADIANT_URANIUM,
        SPECTRAL_SUNNARIUM_PART,
        ENDER_SUNNARIUM_PART,
        // chunks
        SUNNARIUM,
        ENRICHED_SUNNARIUM,
        SPECTRAL_SUNNARIUM,
        ENDER_SUNNARIUM,
        // alloys
        SUNNARIUM_ALLOY,
        ENRICHED_SUNNARIUM_ALLOY,
        SPECTRAL_SUNNARIUM_ALLOY,
        ENDER_SUNNARIUM_ALLOY,
        // glass panes
        IRRADIANT_GLASS_PANE,
        SPECTRAL_GLASS_PANE,
        // plates
        IRIDIUM_IRON_PLATE,
        REINFORCED_IRIDIUM_IRON_PLATE,
        IRRADIANT_REINFORCED_PLATE,
        SPECTRAL_REINFORCED_PLATE,
        //cores
        QUANTUM_CORE,
        PHOTONIC_CORE,
        // mics
        IRIDIUM_INGOT,
        MT_CORE;

        public static final Component[] VALUES = values();

        public ItemStack getStack(int count) {
            return new ItemStack(COMPONENT, count, ordinal());
        }

        public ItemStack getStack() {
            return getStack(1);
        }
    }

    public enum Panels {
        ADVANCED,
        HYBRID,
        ULTIMATE,
        QUANTUM,
        QUANTUM_GEN,
        SPECTRAL,
        SINGULAR,
        LIGHT_ABSORBING,
        PHOTONIC;

        public ItemStack getStack(int count) {
            return new ItemStack(ASP_META_BLOCK, count, ordinal());
        }

        public ItemStack getStack() {
            return getStack(1);
        }
    }
}
