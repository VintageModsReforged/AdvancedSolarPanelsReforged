package reforged.ic2.addons.asp.items;

import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import reforged.ic2.addons.asp.AdvancedSolarPanelsConfig;
import reforged.ic2.addons.asp.utils.EnergyUtils;

import java.util.List;

public class ItemBlockAdvancedSolarPanelsMeta extends ItemBlock {

    public static String[] names = new String[] {
            "advanced",
            "hybrid",
            "ultimate",
            "quantum",
            "quantum.generator",
            "spectral",
            "singular",
            "light_absorbing",
            "photonic"
    };

    public ItemBlockAdvancedSolarPanelsMeta(int id) {
        super(id);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        int meta = stack.getItemDamage();
        if (meta != 4) {
            AdvancedSolarPanelsConfig.SolarConfig energyValues;
            if (meta == 0) {
                energyValues = AdvancedSolarPanelsConfig.ADVANCED;
            } else if (meta == 1) {
                energyValues = AdvancedSolarPanelsConfig.HYBRID;
            } else if (meta == 2) {
                energyValues = AdvancedSolarPanelsConfig.ULTIMATE;
            } else if (meta == 3) {
                energyValues = AdvancedSolarPanelsConfig.QUANTUM;
            } else if (meta == 5) {
                energyValues = AdvancedSolarPanelsConfig.SPECTRAL;
            } else if (meta == 6) {
                energyValues = AdvancedSolarPanelsConfig.SINGULAR;
            } else if (meta == 7) {
                energyValues = AdvancedSolarPanelsConfig.LIGHT_ABSORBING;
            } else {
                energyValues = AdvancedSolarPanelsConfig.PHOTONIC;
            }
            list.add(FormattedTranslator.GRAY.format("tooltip.info.solar.tier", FormattedTranslator.AQUA.literal(energyValues.getTier() + "")));
            list.add(FormattedTranslator.GRAY.format("tooltip.info.solar.gen.day", FormattedTranslator.AQUA.literal(energyValues.getGenDay() + "")));
            list.add(FormattedTranslator.GRAY.format("tooltip.info.solar.gen.night", FormattedTranslator.AQUA.literal(energyValues.getGenNight() + "")));
            list.add(FormattedTranslator.GRAY.format("message.info.solar.max.out", FormattedTranslator.AQUA.literal(EnergyUtils.getPowerFromTier(energyValues.getTier()) + "")));
        } else {
            list.add(FormattedTranslator.GRAY.format("tooltip.info.solar.tier", FormattedTranslator.AQUA.literal(6 + "")));
            list.add(FormattedTranslator.GRAY.format("message.info.solar.max.out", FormattedTranslator.AQUA.literal(EnergyUtils.getPowerFromTier(6) + "")));
        }
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        return "block." + (meta == 4 ? "" : "solar.") + names[meta];
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        FormattedTranslator format;
        int meta = stack.getItemDamage();
        if (meta == 0) {
            format = FormattedTranslator.YELLOW; // advanced
        } else if (meta == 1) {
            format = FormattedTranslator.AQUA; // hybrid
        } else if (meta == 2) {
            format = FormattedTranslator.LIGHT_PURPLE; // ultimate
        } else if (meta == 3) {
            format = FormattedTranslator.DARK_AQUA; // quantum
        } else if (meta == 5) {
            format = FormattedTranslator.GOLD; // spectral
        } else if (meta == 6) {
            format = FormattedTranslator.GRAY; // singular
        } else if (meta == 7 || meta == 4) {
            format = FormattedTranslator.RED; // light absorbing
        } else {
            format = FormattedTranslator.GREEN; // photonic
        }

        return format.literal(super.getItemDisplayName(stack));
    }
}
