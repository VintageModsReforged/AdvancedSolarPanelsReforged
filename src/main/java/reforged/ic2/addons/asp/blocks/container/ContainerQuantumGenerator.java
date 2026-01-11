package reforged.ic2.addons.asp.blocks.container;

import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import reforged.ic2.addons.asp.AdvancedSolarPanels;
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
        if (iCrafting instanceof EntityPlayerMP) {
            AdvancedSolarPanels.network.sendTilePacket((EntityPlayerMP) iCrafting, tile);
        }

        packets = tile.packets;
        packetEnergy = tile.packetEnergy;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (packets != tile.packets || packetEnergy != tile.packetEnergy) {
            for (Object crafter : this.crafters) {
                if (crafter instanceof EntityPlayerMP) {
                    AdvancedSolarPanels.network.sendTilePacket((EntityPlayerMP) crafter, tile);
                }
            }
            // update cache
            packets = tile.packets;
            packetEnergy = tile.packetEnergy;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }
}
