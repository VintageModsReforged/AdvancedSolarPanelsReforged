package reforged.ic2.addons.asp.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.vintage.core.platform.config.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import reforged.ic2.addons.asp.ASPBlocksItems;
import reforged.ic2.addons.asp.ASPRecipes;
import reforged.ic2.addons.asp.AdvancedSolarPanelsConfig;
import reforged.ic2.addons.asp.References;
import reforged.ic2.addons.asp.utils.molecular.MTRecipeManager;

public class CommonProxy {

    // Config
    AdvancedSolarPanelsConfig CONFIG;
    ConfigHandler CONFIG_HANDLER = new ConfigHandler(References.MOD_ID);

    public void preInit(FMLPreInitializationEvent e) {
        CONFIG = new AdvancedSolarPanelsConfig();
        CONFIG_HANDLER.initIDs(CONFIG);
    }

    public void init(FMLInitializationEvent e) {
        CONFIG_HANDLER.confirmIDs(CONFIG);
        ASPBlocksItems.init();
    }

    public void postInit(FMLPostInitializationEvent e) {
        CONFIG_HANDLER.confirmOwnership(CONFIG);
        MTRecipeManager.instance.initRecipes();
        ASPRecipes.initRecipes();
    }

    public int addArmor(String armorName) {
        throw new UnsupportedOperationException("Can't use this method on servers!");
    }

    public boolean isSneakKeyDown() {
        throw new UnsupportedOperationException("Can't use this method on servers!");
    }
}
