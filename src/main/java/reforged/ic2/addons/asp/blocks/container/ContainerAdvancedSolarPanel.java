package reforged.ic2.addons.asp.blocks.container;

import ic2.core.ContainerBase;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import reforged.ic2.addons.asp.tiles.TileEntityAdvancedSolarPanel;

public class ContainerAdvancedSolarPanel extends ContainerBase {

    TileEntityAdvancedSolarPanel tile;
    int storage;
    TileEntityAdvancedSolarPanel.GenerationState generationState;

    public ContainerAdvancedSolarPanel(InventoryPlayer inventoryPlayer, TileEntityAdvancedSolarPanel tile) {
        super(tile);
        this.tile = tile;
        this.storage = 0;
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tile.chargeSlot, i, 17 + i * 18, 59));
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++)
                addSlotToContainer(new Slot(inventoryPlayer, col + row * 9 + 9, 17 + col * 18, 86 + row * 18));
        }
        for (int col = 0; col < 9; col++)
            addSlotToContainer(new Slot(inventoryPlayer, col, 17 + col * 18, 144));
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 0, this.tile.generationState.ordinal());
        crafting.sendProgressBarUpdate(this, 1, this.tile.storage & 0xFFFF);
        crafting.sendProgressBarUpdate(this, 2, this.tile.storage >>> 16);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting crafting = (ICrafting) this.crafters.get(i);
            crafting.sendProgressBarUpdate(this, 0, this.tile.generationState.ordinal());
            crafting.sendProgressBarUpdate(this, 1, this.tile.storage & 0xFFFF);
            crafting.sendProgressBarUpdate(this, 2, this.tile.storage >>> 16);
        }

        this.storage = tile.storage;
        this.generationState = this.tile.generationState;
    }

    @Override
    public void updateProgressBar(int index, int value) {
        if (index == 0) {
            this.tile.generationState = TileEntityAdvancedSolarPanel.GenerationState.values()[value];
        }
        if (index == 1) {
            this.tile.storage = this.tile.storage & 0xFFFF0000 | value;
        }
        if (index == 2) {
            this.tile.storage = this.tile.storage & 0xFFFF | value << 16;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }
}
