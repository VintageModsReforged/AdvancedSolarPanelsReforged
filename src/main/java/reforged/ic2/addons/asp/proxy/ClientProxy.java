package reforged.ic2.addons.asp.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import reforged.ic2.addons.asp.client.AdvancedSolarPanelsBlockRenderer;
import reforged.ic2.addons.asp.client.BlockMolecularTransformerRenderer;
import reforged.ic2.addons.asp.client.TileMolecularTransformerRenderer;
import reforged.ic2.addons.asp.tiles.TileEntityMolecularTransformer;

public class ClientProxy extends CommonProxy {

    public static final int solarPanelRenderId = RenderingRegistry.getNextAvailableRenderId();
    public static final int transformerRenderId = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        RenderingRegistry.registerBlockHandler(new AdvancedSolarPanelsBlockRenderer());
        RenderingRegistry.registerBlockHandler(new BlockMolecularTransformerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMolecularTransformer.class, new TileMolecularTransformerRenderer());
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @Override
    public int addArmor(String armorName) {
        return RenderingRegistry.addNewArmourRendererPrefix(armorName);
    }

    @Override
    public boolean isSneakKeyDown() {
        return Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.keyCode);
    }
}
