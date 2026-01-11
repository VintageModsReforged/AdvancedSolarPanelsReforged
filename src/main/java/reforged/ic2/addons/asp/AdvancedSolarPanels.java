package reforged.ic2.addons.asp;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import mods.vintage.core.platform.lang.LangManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import reforged.ic2.addons.asp.compat.waila.WailaPlugin;
import reforged.ic2.addons.asp.network.ASPNetwork;
import reforged.ic2.addons.asp.network.ASPNetworkClient;
import reforged.ic2.addons.asp.proxy.CommonProxy;

import java.util.logging.Logger;

@Mod(modid = References.MOD_ID, useMetadata = true)
@NetworkMod(clientSideRequired = true,
        clientPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = {References.NET_ID}, packetHandler = ASPNetworkClient.class),
        serverPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = {References.NET_ID}, packetHandler = ASPNetwork.class))
public class AdvancedSolarPanels {

    public static final Logger LOG = Logger.getLogger(References.NAME);
    public static final CreativeTabs TAB = new CreativeTabs(References.MOD_ID) {
        @Override
        public ItemStack getIconItemStack() {
            return ASPBlocksItems.Panels.ULTIMATE.getStack();
        }
    };

    @SidedProxy(clientSide = References.PROXY_CLIENT, serverSide = References.PROXY_COMMON)
    public static CommonProxy PROXY;

    @SidedProxy(clientSide = References.NET_CLIENT, serverSide = References.NET_SERVER)
    public static ASPNetwork network;

    public AdvancedSolarPanels() {
        LOG.setParent(FMLLog.getLogger());
    }

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent e) {
        PROXY.preInit(e);
        LangManager.INSTANCE.loadCreativeTabName(References.MOD_ID, References.NAME);
    }

    @Mod.Init
    public void init(FMLInitializationEvent e) {
        PROXY.init(e);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent e) {
        PROXY.postInit(e);
        if (Loader.isModLoaded("BlockHelperAddons")) {
            WailaPlugin.init();
        }
    }
}
