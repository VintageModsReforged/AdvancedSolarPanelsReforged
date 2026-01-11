package reforged.ic2.addons.asp.network;

import net.minecraft.network.packet.Packet250CustomPayload;
import reforged.ic2.addons.asp.References;

public interface IASPPacket {

    String CHANNEL = References.NET_ID;
    Packet250CustomPayload encode();
    ASPPackets getId();
}
