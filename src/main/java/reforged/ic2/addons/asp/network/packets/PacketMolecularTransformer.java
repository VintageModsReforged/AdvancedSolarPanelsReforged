package reforged.ic2.addons.asp.network.packets;

import net.minecraft.network.packet.Packet250CustomPayload;
import reforged.ic2.addons.asp.network.ASPPackets;
import reforged.ic2.addons.asp.network.IASPPacket;
import reforged.ic2.addons.asp.tiles.TileEntityMolecularTransformer;

import java.io.*;

public class PacketMolecularTransformer implements IASPPacket {

    public int x, y, z;

    public int lastRecipeEnergyUsed;
    public int lastRecipeEnergyPerOperation;
    public boolean doWork;
    public short lastProgress;
    public int lastRecipeNumber;
    public int inputEU;

    public PacketMolecularTransformer(DataInputStream dis) throws IOException {
        this.x = dis.readInt();
        this.y = dis.readInt();
        this.z = dis.readInt();

        this.lastRecipeEnergyUsed = dis.readInt();
        this.lastRecipeEnergyPerOperation = dis.readInt();
        this.doWork = dis.readBoolean();
        this.lastProgress = dis.readShort();
        this.lastRecipeNumber = dis.readInt();
        this.inputEU = dis.readInt();
    }

    public PacketMolecularTransformer(TileEntityMolecularTransformer tile) {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;

        this.lastRecipeEnergyUsed = tile.lastRecipeEnergyUsed;
        this.lastRecipeEnergyPerOperation = tile.lastRecipeEnergyPerOperation;
        this.doWork = tile.doWork;
        this.lastProgress = tile.lastProgress;
        this.lastRecipeNumber = tile.lastRecipeNumber;
        this.inputEU = tile.inputEU;
    }

    @Override
    public Packet250CustomPayload encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeByte(getId().ordinal());
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(z);
            dos.writeInt(lastRecipeEnergyUsed);
            dos.writeInt(lastRecipeEnergyPerOperation);
            dos.writeBoolean(doWork);
            dos.writeShort(lastProgress);
            dos.writeInt(lastRecipeNumber);
            dos.writeInt(inputEU);
            dos.flush();

            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = CHANNEL;
            packet.data = bos.toByteArray();
            packet.length = bos.size();
            packet.isChunkDataPacket = false;
            return packet;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ASPPackets getId() {
        return ASPPackets.TRANSFORMER;
    }
}
