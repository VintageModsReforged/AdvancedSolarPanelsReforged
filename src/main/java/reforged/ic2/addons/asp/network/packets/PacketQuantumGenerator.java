package reforged.ic2.addons.asp.network.packets;

import net.minecraft.network.packet.Packet250CustomPayload;
import reforged.ic2.addons.asp.network.ASPPackets;
import reforged.ic2.addons.asp.network.IASPPacket;
import reforged.ic2.addons.asp.tiles.TileEntityQuantumGenerator;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketQuantumGenerator implements IASPPacket {

    public int x, y, z;
    public int packetEnergy;
    public int packets;

    public PacketQuantumGenerator(DataInputStream dis) throws IOException {
        this.x = dis.readInt();
        this.y = dis.readInt();
        this.z = dis.readInt();
        this.packets = dis.readInt();
        this.packetEnergy = dis.readInt();
    }

    public PacketQuantumGenerator(TileEntityQuantumGenerator tile) {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
        this.packets = tile.packets;
        this.packetEnergy = tile.packetEnergy;
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
            dos.writeInt(packets);
            dos.writeInt(packetEnergy);
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
        return ASPPackets.Q_GENERATOR;
    }
}
