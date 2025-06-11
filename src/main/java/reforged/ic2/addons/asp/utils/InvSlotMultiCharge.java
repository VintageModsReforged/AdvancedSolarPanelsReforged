package reforged.ic2.addons.asp.utils;

import ic2.api.item.IElectricItem;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlotMultiCharge extends InvSlot {
    private final int tier;

    public InvSlotMultiCharge(TileEntityInventory base, int oldStartIndex, int tier, int size) {
        super(base, "charge", oldStartIndex, Access.IO, size, InvSide.TOP);
        this.tier = tier;
    }

    public boolean accepts(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof IElectricItem && ((IElectricItem) item).getTier(itemStack) <= this.tier;
    }
}
