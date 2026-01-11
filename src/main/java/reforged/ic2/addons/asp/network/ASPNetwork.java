package reforged.ic2.addons.asp.network;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class ASPNetwork implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager iNetworkManager, Packet250CustomPayload packet250CustomPayload, Player player) {

    }

    public void sendTilePacket(EntityPlayerMP player, INetworkedTile tile) {
        Packet250CustomPayload payload = tile.getPacket();
        if (payload != null) {
            player.playerNetServerHandler.sendPacketToPlayer(payload);
        }
    }
}
