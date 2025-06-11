package reforged.ic2.addons.asp.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import reforged.ic2.addons.asp.proxy.ClientProxy;
import reforged.ic2.addons.asp.tiles.TileEntityMolecularTransformer;

@SideOnly(Side.CLIENT)
public class BlockMolecularTransformerRenderer implements ISimpleBlockRenderingHandler {

    private final TileEntityMolecularTransformer tile = new TileEntityMolecularTransformer();

    @Override
    public void renderInventoryBlock(Block block, int i, int i1, RenderBlocks renderBlocks) {
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        TileEntityRenderer.instance.renderTileEntityAt(tile, 0, 0, 0, 0);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess iBlockAccess, int i, int i1, int i2, Block block, int i3, RenderBlocks renderBlocks) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return ClientProxy.transformerRenderId;
    }
}
