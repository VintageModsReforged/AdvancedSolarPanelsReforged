package reforged.ic2.addons.asp.network;

import cpw.mods.fml.common.network.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reforged.ic2.addons.asp.References;
import reforged.ic2.addons.asp.network.packets.PacketAdvSolarPanel;
import reforged.ic2.addons.asp.network.packets.PacketMolecularTransformer;
import reforged.ic2.addons.asp.network.packets.PacketQuantumGenerator;
import reforged.ic2.addons.asp.tiles.TileEntityAdvancedSolarPanel;
import reforged.ic2.addons.asp.tiles.TileEntityMolecularTransformer;
import reforged.ic2.addons.asp.tiles.TileEntityQuantumGenerator;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ASPNetworkClient extends ASPNetwork {

    @Override
    public void onPacketData(INetworkManager iNetworkManager, Packet250CustomPayload packet, Player player) {
        if (!packet.channel.equals(References.NET_ID)) return;
        ByteArrayInputStream buffer = new ByteArrayInputStream(packet.data, 1, packet.data.length - 1);
        try {
            byte id = packet.data[0];
            ASPPackets packetType = ASPPackets.values()[id];
            DataInputStream dis = new DataInputStream(buffer);
            World world = Minecraft.getMinecraft().theWorld;
            switch (packetType) {
                case SOLAR:
                    PacketAdvSolarPanel solar = new PacketAdvSolarPanel(dis);
                    // get the tile in the world
                    TileEntity solarTE = world.getBlockTileEntity(solar.x, solar.y, solar.z);
                    if (solarTE instanceof TileEntityAdvancedSolarPanel) {
                        TileEntityAdvancedSolarPanel tile = (TileEntityAdvancedSolarPanel) solarTE;
                        tile.storage = solar.storage;
                        tile.generationState = TileEntityAdvancedSolarPanel.GenerationState.values()[solar.genStateOrdinal];
                    }
                    break;
                case TRANSFORMER:
                    PacketMolecularTransformer transformer = new PacketMolecularTransformer(dis);
                    TileEntity molecularTE = world.getBlockTileEntity(transformer.x, transformer.y, transformer.z);
                    if (molecularTE instanceof TileEntityMolecularTransformer) {
                        TileEntityMolecularTransformer tile = (TileEntityMolecularTransformer) molecularTE;
                        tile.lastRecipeEnergyUsed = transformer.lastRecipeEnergyUsed;
                        tile.lastRecipeEnergyPerOperation = transformer.lastRecipeEnergyPerOperation;
                        tile.doWork = transformer.doWork;
                        tile.lastProgress = transformer.lastProgress;
                        tile.lastRecipeNumber = transformer.lastRecipeNumber;
                        tile.inputEU = transformer.inputEU;
                    }
                    break;
                case Q_GENERATOR:
                    PacketQuantumGenerator generator = new PacketQuantumGenerator(dis);
                    TileEntity generatorTE = world.getBlockTileEntity(generator.x, generator.y, generator.z);
                    if (generatorTE instanceof TileEntityQuantumGenerator) {
                        TileEntityQuantumGenerator tile = (TileEntityQuantumGenerator) generatorTE;
                        tile.packets = generator.packets;
                        tile.packetEnergy = generator.packetEnergy;
                    }
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
