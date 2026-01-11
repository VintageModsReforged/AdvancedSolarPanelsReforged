package reforged.ic2.addons.asp;

import net.minecraft.util.StatCollector;

public class References {

    public static final String MOD_ID = "AdvancedSolarPanels";
    public static final String NAME = "Advanced Solar Panels";

    // proxy
    public static final String PROXY_CLIENT = "reforged.ic2.addons.asp.proxy.ClientProxy";
    public static final String PROXY_COMMON = "reforged.ic2.addons.asp.proxy.CommonProxy";

    // network
    public static final String NET_ID = "ASPNetwork";
    public static final String NET_CLIENT = "reforged.ic2.addons.asp.network.ASPNetworkClient";
    public static final String NET_SERVER = "reforged.ic2.addons.asp.network.ASPNetwork";

    public static final String SNEAK_KEY = StatCollector.translateToLocal("key.sneak");
}
