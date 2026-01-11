package reforged.ic2.addons.asp.network.packets;

import net.minecraft.network.packet.Packet250CustomPayload;
import reforged.ic2.addons.asp.network.ASPPackets;
import reforged.ic2.addons.asp.network.IASPPacket;
import reforged.ic2.addons.asp.tiles.TileEntityAdvancedSolarPanel;

import java.io.*;

public class PacketAdvSolarPanel implements IASPPacket {

    public int x, y, z;
    public int storage;
    public int genStateOrdinal;

    public PacketAdvSolarPanel(DataInputStream dis) throws IOException {
        this.x = dis.readInt();
        this.y = dis.readInt();
        this.z = dis.readInt();
        this.storage = dis.readInt();
        this.genStateOrdinal = dis.readInt();
    }

    public PacketAdvSolarPanel(TileEntityAdvancedSolarPanel tile) {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
        this.storage = tile.storage;
        this.genStateOrdinal = tile.generationState.ordinal();
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
            dos.writeInt(storage);
            dos.writeInt(genStateOrdinal);
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
        return ASPPackets.SOLAR;
    }
}
