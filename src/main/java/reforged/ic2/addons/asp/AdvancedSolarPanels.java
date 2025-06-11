package reforged.ic2.addons.asp;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mods.vintage.core.platform.lang.ILangProvider;
import mods.vintage.core.platform.lang.LangManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import reforged.ic2.addons.asp.proxy.CommonProxy;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Mod(modid = References.MOD_ID, name = References.NAME, useMetadata = true)
public class AdvancedSolarPanels implements ILangProvider {

    public static final Logger LOG = Logger.getLogger(References.NAME);
    public static final CreativeTabs TAB = new CreativeTabs(References.MOD_ID) {
        @Override
        public ItemStack getIconItemStack() {
            return new ItemStack(ASPBlocksItems.ASP_META_BLOCK, 1, 2);
        }
    };

    @SidedProxy(clientSide = References.PROXY_CLIENT, serverSide = References.PROXY_COMMON)
    public static CommonProxy PROXY;

    public AdvancedSolarPanels() {
        LOG.setParent(FMLLog.getLogger());
    }

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent e) {
        PROXY.preInit(e);
        LangManager.THIS.registerLangProvider(this);
        LangManager.THIS.loadCreativeTabName(References.MOD_ID, References.NAME);
    }

    @Mod.Init
    public void init(FMLInitializationEvent e) {
        PROXY.init(e);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent e) {
        PROXY.postInit(e);
    }

    @Override
    public String getModid() {
        return References.MOD_ID;
    }

    @Override
    public List<String> getLocalizationList() {
        return Arrays.asList(AdvancedSolarPanelsConfig.LANGS);
    }
}
