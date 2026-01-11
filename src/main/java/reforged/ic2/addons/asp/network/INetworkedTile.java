package reforged.ic2.addons.asp.network;

import net.minecraft.network.packet.Packet250CustomPayload;

public interface INetworkedTile {

    Packet250CustomPayload getPacket();
}
