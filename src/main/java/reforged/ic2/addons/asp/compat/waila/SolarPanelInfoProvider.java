package reforged.ic2.addons.asp.compat.waila;

import mods.vintage.core.helpers.ElectricHelper;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import reforged.ic2.addons.asp.tiles.TileEntityAdvancedSolarPanel;
import reforged.ic2.addons.asp.tiles.TileEntityMolecularTransformer;
import reforged.ic2.addons.asp.tiles.TileEntityQuantumGenerator;
import reforged.ic2.addons.asp.utils.molecular.MTRecipeManager;
import reforged.ic2.addons.asp.utils.molecular.RecipeRecord;
import reforged.mods.blockhelper.addons.utils.ColorUtils;
import reforged.mods.blockhelper.addons.utils.Formatter;
import reforged.mods.blockhelper.addons.utils.InfoProvider;
import reforged.mods.blockhelper.addons.utils.interfaces.IWailaHelper;

public class SolarPanelInfoProvider extends InfoProvider {

    public static final SolarPanelInfoProvider THIS = new SolarPanelInfoProvider();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockeEntity, EntityPlayer player) {
        if (blockeEntity instanceof TileEntityMolecularTransformer) {
            TileEntityMolecularTransformer transformer = (TileEntityMolecularTransformer) blockeEntity;
            text(helper, tier(6));
            text(helper, maxIn(ElectricHelper.getMaxInputFromTier(6)));

            int lastRecipe = transformer.lastRecipeNumber;
            if (lastRecipe != -1) {
                RecipeRecord recipe = MTRecipeManager.instance.getRecipes().get(lastRecipe);
                text(helper, Translator.GREEN.format("message.info.molecular.input", Translator.WHITE.literal(recipe.input.stackSize + "*") + Translator.AQUA.literal(recipe.input.getDisplayName())));
                text(helper, Translator.RED.format("message.info.molecular.output", Translator.WHITE.literal(recipe.output.stackSize + "*") + Translator.AQUA.literal(recipe.output.getDisplayName())));
            }
            int progress = transformer.lastProgress;
            if (progress > 0) {
                bar(helper, transformer.lastProgress, 100, Translator.WHITE.format("message.info.molecular.progress", transformer.lastProgress), ColorUtils.CYAN);
            }
        }
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
