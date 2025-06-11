package reforged.ic2.addons.asp.items;

import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemMolecularTransformer extends ItemBlock {

    public ItemMolecularTransformer(int id) {
        super(id);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        list.add(FormattedTranslator.GRAY.format("tooltip.info.solar.tier", FormattedTranslator.AQUA.literal(6 + "")));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "block.molecular.transformer";
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        return FormattedTranslator.AQUA.literal(super.getItemDisplayName(stack));
    }
}
