package reforged.ic2.addons.asp.blocks.container;

import ic2.core.ContainerBase;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import reforged.ic2.addons.asp.AdvancedSolarPanels;
import reforged.ic2.addons.asp.tiles.TileEntityAdvancedSolarPanel;

public class ContainerAdvancedSolarPanel extends ContainerBase {

    TileEntityAdvancedSolarPanel tile;
    // cache last sent values for this player
    private int lastStorage = -1;
    private TileEntityAdvancedSolarPanel.GenerationState lastGenState = null;

    public ContainerAdvancedSolarPanel(InventoryPlayer inventoryPlayer, TileEntityAdvancedSolarPanel tile) {
        super(tile);
        this.tile = tile;
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

        if (crafting instanceof EntityPlayerMP) {
            AdvancedSolarPanels.network.sendTilePacket((EntityPlayerMP) crafting, tile);
        }

        // cache last values
        lastStorage = tile.storage;
        lastGenState = tile.generationState;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (lastStorage != tile.storage || lastGenState != tile.generationState) {
            for (Object crafter : this.crafters) {
                if (crafter instanceof EntityPlayerMP) {
                    AdvancedSolarPanels.network.sendTilePacket((EntityPlayerMP) crafter, tile);
                }
            }
            // update cache
            lastStorage = tile.storage;
            lastGenState = tile.generationState;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }
}
