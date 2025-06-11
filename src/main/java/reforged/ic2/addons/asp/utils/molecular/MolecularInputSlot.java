package reforged.ic2.addons.asp.utils.molecular;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotProcessable;
import net.minecraft.item.ItemStack;

public class MolecularInputSlot extends InvSlotProcessable {

    public MolecularInputSlot(TileEntityInventory base, String name, int oldStartIndex, int count) {
        super(base, name, oldStartIndex, count);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return MTRecipeManager.instance.hasRecipeFor(stack);
    }

    @Override
    public ItemStack process(boolean b) {
        return null;
    }
}
