package reforged.ic2.addons.asp.blocks.container;

import ic2.core.ContainerBase;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import reforged.ic2.addons.asp.AdvancedSolarPanels;
import reforged.ic2.addons.asp.tiles.TileEntityMolecularTransformer;

public class ContainerMolecularTransformer extends ContainerBase {

    public TileEntityMolecularTransformer tile;

    private boolean lastDoWork = false;
    private short lastProgress = -1;
    private int lastRecipeNumber = -1;

    public ContainerMolecularTransformer(InventoryPlayer inventoryPlayer, TileEntityMolecularTransformer tile) {
        super(tile);
        this.tile = tile;
        addSlotToContainer(new SlotInvSlot(tile.inputSlot, 0, 20, 27));
        addSlotToContainer(new SlotInvSlot(tile.outputSlot, 0, 20, 68));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++)
                addSlotToContainer(new Slot(inventoryPlayer, col + row * 9 + 9, 18 + col * 21, 98 + row * 21));
        }
        for (int col = 0; col < 9; col++)
            addSlotToContainer(new Slot(inventoryPlayer, col, 18 + col * 21, 165));
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafter) {
        super.addCraftingToCrafters(crafter);
        if (crafter instanceof EntityPlayerMP) {
            AdvancedSolarPanels.network.sendTilePacket((EntityPlayerMP) crafter, tile);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        boolean changed = lastDoWork != tile.doWork ||
                        lastProgress != tile.lastProgress ||
                        lastRecipeNumber != tile.lastRecipeNumber;

        if (changed) {
            for (Object crafter : this.crafters) {
                if (crafter instanceof EntityPlayerMP) {
                    AdvancedSolarPanels.network.sendTilePacket((EntityPlayerMP) crafter, tile);
                }
            }

            // update cache
            lastDoWork = tile.doWork;
            lastProgress = tile.lastProgress;
            lastRecipeNumber = tile.lastRecipeNumber;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }
}
