package reforged.ic2.addons.asp.blocks.container;

import ic2.core.ContainerBase;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import reforged.ic2.addons.asp.tiles.TileEntityMolecularTransformer;

public class ContainerMolecularTransformer extends ContainerBase {

    public TileEntityMolecularTransformer tile;

    private short lastProgress;
    private int doWork;
    private int lastRecipeNumber;

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

        sendShortSplit(crafter, 0, tile.lastRecipeEnergyUsed);
        sendShortSplit(crafter, 2, tile.lastRecipeEnergyPerOperation);
        crafter.sendProgressBarUpdate(this, 4, tile.doWork ? 1 : 0);
        crafter.sendProgressBarUpdate(this, 5, tile.lastProgress);
        crafter.sendProgressBarUpdate(this, 6, tile.lastRecipeNumber);
        sendShortSplit(crafter, 7, tile.inputEU);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting crafter = (ICrafting) this.crafters.get(i);

            sendShortSplit(crafter, 0, tile.lastRecipeEnergyUsed);
            sendShortSplit(crafter, 2, tile.lastRecipeEnergyPerOperation);
            sendShortSplit(crafter, 7, tile.inputEU);

            if (this.doWork != (tile.doWork ? 1 : 0)) {
                crafter.sendProgressBarUpdate(this, 4, tile.doWork ? 1 : 0);
            }
            if (this.lastProgress != tile.lastProgress) {
                crafter.sendProgressBarUpdate(this, 5, tile.lastProgress);
            }
            if (this.lastRecipeNumber != tile.lastRecipeNumber) {
                crafter.sendProgressBarUpdate(this, 6, tile.lastRecipeNumber);
            }
        }

        this.doWork = tile.doWork ? 1 : 0;
        this.lastProgress = tile.lastProgress;
        this.lastRecipeNumber = tile.lastRecipeNumber;
    }

    @Override
    public void updateProgressBar(int index, int value) {
        switch (index) {
            case 0:
                tile.lastRecipeEnergyUsed = (tile.lastRecipeEnergyUsed & 0xFFFF0000) | (value & 0xFFFF);
                break;
            case 1:
                tile.lastRecipeEnergyUsed = (tile.lastRecipeEnergyUsed & 0xFFFF) | (value << 16);
                break;
            case 2:
                tile.lastRecipeEnergyPerOperation = (tile.lastRecipeEnergyPerOperation & 0xFFFF0000) | (value & 0xFFFF);
                break;
            case 3:
                tile.lastRecipeEnergyPerOperation = (tile.lastRecipeEnergyPerOperation & 0xFFFF) | (value << 16);
                break;
            case 4:
                tile.doWork = (value == 1);
                break;
            case 5:
                tile.lastProgress = (short) value;
                break;
            case 6:
                tile.lastRecipeNumber = value;
                break;
            case 7:
                tile.inputEU = (tile.inputEU & 0xFFFF0000) | (value & 0xFFFF);
                break;
            case 8:
                tile.inputEU = (tile.inputEU & 0xFFFF) | (value << 16);
                break;
        }
    }

    private void sendShortSplit(ICrafting crafter, int baseIndex, int value) {
        crafter.sendProgressBarUpdate(this, baseIndex, value & 0xFFFF);
        crafter.sendProgressBarUpdate(this, baseIndex + 1, value >>> 16);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }
}
