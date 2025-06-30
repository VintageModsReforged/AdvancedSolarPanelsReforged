package reforged.ic2.addons.asp.compat.waila;

import reforged.mods.blockhelper.addons.base.WailaCommonHandler;

public class WailaPlugin {

    public static void init() {
        WailaCommonHandler.INSTANCE.registerAddonProviders(SolarPanelInfoProvider.THIS);
    }
}
