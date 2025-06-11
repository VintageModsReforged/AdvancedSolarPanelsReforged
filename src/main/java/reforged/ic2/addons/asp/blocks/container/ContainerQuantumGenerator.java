package reforged.ic2.addons.asp.blocks.container;

import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import reforged.ic2.addons.asp.tiles.TileEntityQuantumGenerator;

public class ContainerQuantumGenerator extends ContainerFullInv {

    public TileEntityQuantumGenerator tile;
    public int packets;
    public int packetEnergy;

    public ContainerQuantumGenerator(InventoryPlayer inventoryPlayer, TileEntityQuantumGenerator tile) {
        super(inventoryPlayer.player, tile, 166);
        this.tile = tile;
    }

    @Override
    public void addCraftingToCrafters(ICrafting iCrafting) {
        super.addCraftingToCrafters(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, this.tile.packets);
        iCrafting.sendProgressBarUpdate(this, 1, this.tile.packetEnergy & 0xFFFF);
        iCrafting.sendProgressBarUpdate(this, 2, this.tile.packetEnergy >>> 16);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting crafting = (ICrafting) crafter;
            if (this.packets != this.tile.packets) {
                crafting.sendProgressBarUpdate(this, 0, this.tile.packets);
            }
            if (this.packetEnergy != this.tile.packetEnergy) {
                crafting.sendProgressBarUpdate(this, 1, this.tile.packetEnergy & 0xFFFF);
                crafting.sendProgressBarUpdate(this, 2, this.tile.packetEnergy >>> 16);
            }
        }
        this.packets = this.tile.packets;
        this.packetEnergy = this.tile.packetEnergy;
    }

    @Override
    public void updateProgressBar(int index, int value) {
        if (index == 0) {
            this.tile.packets = value;
        }
        if (index == 1) {
            this.tile.packetEnergy = (this.tile.packetEnergy & 0xFFFF0000) | value;
        }
        if (index == 2) {
            this.tile.packetEnergy = (this.tile.packetEnergy & 0xFFFF) | (value << 16);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }
}
