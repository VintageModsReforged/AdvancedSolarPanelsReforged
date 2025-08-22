package reforged.ic2.addons.asp.tiles;

import ic2.api.Direction;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import mods.vintage.core.helpers.ElectricHelper;
import mods.vintage.core.platform.lang.Translator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import reforged.ic2.addons.asp.blocks.container.ContainerQuantumGenerator;
import reforged.ic2.addons.asp.blocks.gui.GuiQuantumGenerator;

public class TileEntityQuantumGenerator extends TileEntityBaseGenerator implements INetworkClientTileEntityEventListener {

    public int packets = 4; // number of packets to send
    public int packetEnergy = 32; // energy in each packet

    public TileEntityQuantumGenerator() {
        super(0, 0);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("packets", this.packets);
        compound.setInteger("packetEnergy", this.packetEnergy);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.packets = compound.getInteger("packets");
        this.packetEnergy = compound.getInteger("packetEnergy");
    }

    @Override
    public void updateEntity() {
        if (!IC2.platform.isSimulating()) return;

        int sinkCount = 0;
        // gather sinks
        for (Direction dir : Direction.values()) {
            TileEntity neighbor = worldObj.getBlockTileEntity(
                    xCoord + dir.toForgeDirection().offsetX,
                    yCoord + dir.toForgeDirection().offsetY,
                    zCoord + dir.toForgeDirection().offsetZ
            );

            if (neighbor instanceof IEnergyAcceptor) {
                IEnergyAcceptor sink = (IEnergyAcceptor) neighbor;
                if (sink.acceptsEnergyFrom(this, dir.getInverse())) {
                    sinkCount++;
                }
            }
        }

        // ensure we emit normal amount when no sinks are present
        if (sinkCount == 0) sinkCount = 1;
        // adjust packet size
        int adjustedPacketSize = this.packetEnergy * sinkCount;
        for (int i = 0; i < this.packets; i++) {
            sendEnergy(adjustedPacketSize);
        }
    }

    @Override
    public int gaugeFuelScaled(int i) {
        return 0;
    }

    @Override
    public boolean gainFuel() {
        return false;
    }

    @Override
    public String getInvName() {
        return Translator.WHITE.format("block.quantum.generator.name");
    }

    @Override
    public int getMaxEnergyOutput() {
        return this.packetEnergy;
    }

    @Override
    public boolean emitsEnergyTo(TileEntity tileEntity, Direction direction) {
        return true;
    }

    @Override
    public void onNetworkEvent(EntityPlayer entityPlayer, int key) {
        switch (key) {
            // packets
            case -11:
                changePacket(-10);
                break;
            case -1:
                changePacket(-1);
                break;
            case 1:
                changePacket(1);
                break;
            case 11:
                changePacket(10);
                break;

            // packetEnergy
            case -2222:
                changePacketEnergy(-1000);
                break;
            case -222:
                changePacketEnergy(-100);
                break;
            case -22:
                changePacketEnergy(-10);
                break;
            case -2:
                changePacketEnergy(-1);
                break;
            case 2:
                changePacketEnergy(1);
                break;
            case 22:
                changePacketEnergy(10);
                break;
            case 222:
                changePacketEnergy(100);
                break;
            case 2222:
                changePacketEnergy(1000);
                break;
            case 1000:
                changeTier(0);
                break;
            case 1001:
                changeTier(1);
                break;
            case 1002:
                changeTier(2);
                break;
            case 1003:
                changeTier(3);
                break;
            case 1004:
                changeTier(4);
                break;
            case 1005:
                changeTier(5);
                break;
            case 1006:
                changeTier(6);
                break;
        }
    }

    @Override
    public ContainerBase getGuiContainer(EntityPlayer player) {
        return new ContainerQuantumGenerator(player.inventory, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean b) {
        return new GuiQuantumGenerator(new ContainerQuantumGenerator(entityPlayer.inventory, this));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public void changePacket(int mul) {
        this.packets = MathHelper.clamp_int(this.packets + mul, 1, 64);
    }

    public void changePacketEnergy(int mul) {
        this.packetEnergy = MathHelper.clamp_int(this.packetEnergy + mul, 1, Short.MAX_VALUE + 1);
    }

    public void changeTier(int tier) {
        this.packetEnergy = ElectricHelper.getMaxInputFromTier(tier);
    }
}
