package reforged.ic2.addons.asp.compat.waila;

import mods.vintage.core.helpers.ElectricHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import reforged.ic2.addons.asp.tiles.TileEntityAdvancedSolarPanel;
import reforged.ic2.addons.asp.tiles.TileEntityQuantumGenerator;
import reforged.mods.blockhelper.addons.utils.ColorUtils;
import reforged.mods.blockhelper.addons.utils.Formatter;
import reforged.mods.blockhelper.addons.utils.InfoProvider;
import reforged.mods.blockhelper.addons.utils.interfaces.IWailaHelper;

public class SolarPanelInfoProvider extends InfoProvider {

    public static final SolarPanelInfoProvider THIS = new SolarPanelInfoProvider();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockeEntity, EntityPlayer player) {
        if (blockeEntity instanceof TileEntityQuantumGenerator) {
            TileEntityQuantumGenerator generator = (TileEntityQuantumGenerator) blockeEntity;
            bar(helper, 1, 1, translate("info.energy.infinite"), ColorUtils.RED);
            text(helper, tier(6));
            text(helper, translate("info.generator.output", generator.packetEnergy * generator.packets));
            text(helper, translate("info.generator.max_output", ElectricHelper.getMaxInputFromTier(6) * 64));
        }
        if (blockeEntity instanceof TileEntityAdvancedSolarPanel) {
            TileEntityAdvancedSolarPanel panel = (TileEntityAdvancedSolarPanel) blockeEntity;
            int stored = panel.storage;
            int capacity = panel.maxStorage;
            int generating = panel.isSunVisible() ? panel.dayGen : panel.nightGen;
            int maxOutput = panel.maxOutput;
            bar(helper, stored, capacity, translate("info.energy", Formatter.formatNumber(stored, 2), Formatter.formatNumber(capacity, 2)), ColorUtils.RED);
            text(helper, tier(panel.tier));
            text(helper, translate("info.generator.output", generating));
            text(helper, translate("info.generator.max_output", maxOutput));
        }
    }
}
