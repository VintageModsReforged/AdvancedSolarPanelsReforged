package reforged.ic2.addons.asp.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import reforged.ic2.addons.asp.AdvancedSolarPanels;
import reforged.ic2.addons.asp.blocks.base.BlockAdvancedBlock;
import reforged.ic2.addons.asp.proxy.ClientProxy;

@SideOnly(Side.CLIENT)
public class AdvancedSolarPanelsBlockRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderblocks) {
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderblocks.setRenderBoundsFromBlock(block);

        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        // Start collecting data into a buffer
        tessellator.startDrawingQuads(); // begins a vertex batch
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderblocks.renderFaceYNeg(block, 0.0, 0.0, 0.0,
                renderblocks.getBlockIconFromSideAndMetadata(block, 0, metadata));

        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderblocks.renderFaceYPos(block, 0.0, 0.0, 0.0,
                renderblocks.getBlockIconFromSideAndMetadata(block, 1, metadata));

        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderblocks.renderFaceZNeg(block, 0.0, 0.0, 0.0,
                renderblocks.getBlockIconFromSideAndMetadata(block, 2, metadata));

        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderblocks.renderFaceZPos(block, 0.0, 0.0, 0.0,
                renderblocks.getBlockIconFromSideAndMetadata(block, 3, metadata));

        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceXNeg(block, 0.0, 0.0, 0.0,
                renderblocks.getBlockIconFromSideAndMetadata(block, 4, metadata));

        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceXPos(block, 0.0, 0.0, 0.0,
                renderblocks.getBlockIconFromSideAndMetadata(block, 5, metadata));

        // Thanks radial menu mod and Speiger for teaching me stuff :+1:
        // draw the buffer (flush to GPU)
        tessellator.draw(); // flush the vertex buffer

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderBlocks) {
        ((BlockAdvancedBlock) block).onRender(blockAccess, x, y, z);
        return renderBlocks.renderStandardBlock(block, x, y, z);
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return ClientProxy.solarPanelRenderId;
    }
}
