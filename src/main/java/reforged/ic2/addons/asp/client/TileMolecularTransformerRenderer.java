package reforged.ic2.addons.asp.client;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import reforged.ic2.addons.asp.AdvancedSolarPanels;
import reforged.ic2.addons.asp.client.models.ModelMolecularTransformer;
import reforged.ic2.addons.asp.tiles.TileEntityMolecularTransformer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileMolecularTransformerRenderer extends TileEntitySpecialRenderer {

    private static final String transformerTexture = "/mods/AdvancedSolarPanels/textures/models/molecular_transformer.png";

    private static final String plasmaTexture = "/mods/AdvancedSolarPanels/textures/models/plasma.png";

    private static final String particleTexture = "/mods/AdvancedSolarPanels/textures/models/particles.png";

    public final ModelMolecularTransformer model = new ModelMolecularTransformer();

    private static final Map<List<Serializable>, Integer> textureCache = new HashMap<List<Serializable>, Integer>();

    public int ticker;

    public static int getTextureSize(String path, int paramInt) {
        List<Serializable> key = Arrays.asList(new Serializable[]{path, paramInt});
        if (textureCache.get(key) != null)
            return textureCache.get(key);
        try {
            InputStream inputStream = AdvancedSolarPanels.class.getResourceAsStream("/mods/AdvancedSolarPanels/" + path);
            if (inputStream == null)
                throw new Exception("Image not found: " + path);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            int i = bufferedImage.getWidth() / paramInt;
            textureCache.put(key, i);
            return i;
        } catch (Exception exception) {
            exception.printStackTrace();
            return 16;
        }
    }

    public void renderCore(TileEntity te, double x, double y, double z, float scale) {
        int size1 = 0;
        int size2 = 0;
        this.ticker++;
        if (this.ticker > 161)
            this.ticker = 1;
        size1 = getTextureSize("textures/models/plasma.png", 64);
        size2 = getTextureSize("textures/models/particles.png", 32);
        float f1 = ActiveRenderInfo.rotationX;
        float f2 = ActiveRenderInfo.rotationXZ;
        float f3 = ActiveRenderInfo.rotationZ;
        float f4 = ActiveRenderInfo.rotationYZ;
        float f5 = ActiveRenderInfo.rotationXY;
        float scaleCore = 0.35F;
        float posX = (float) x + 0.5F;
        float posY = (float) y + 0.5F;
        float posZ = (float) z + 0.5F;
        Tessellator tessellator = Tessellator.instance;
        Color color = new Color(12648447);
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        (FMLClientHandler.instance().getClient()).renderEngine.bindTexture(plasmaTexture);
        int i = this.ticker % 16;
        float size4 = (size1 * 4);
        float float_sizeMinus0_01 = size1 - 0.01F;
        float x0 = ((i % 4 * size1) + 0.0F) / size4;
        float x1 = ((i % 4 * size1) + float_sizeMinus0_01) / size4;
        float x2 = ((i / 4 * size1) + 0.0F) / size4;
        float x3 = ((i / 4 * size1) + float_sizeMinus0_01) / size4;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
        tessellator.addVertexWithUV((posX - f1 * scaleCore - f4 * scaleCore), (posY - f2 * scaleCore),
                (posZ - f3 * scaleCore - f5 * scaleCore), x1, x3);
        tessellator.addVertexWithUV((posX - f1 * scaleCore + f4 * scaleCore), (posY + f2 * scaleCore),
                (posZ - f3 * scaleCore + f5 * scaleCore), x1, x2);
        tessellator.addVertexWithUV((posX + f1 * scaleCore + f4 * scaleCore), (posY + f2 * scaleCore),
                (posZ + f3 * scaleCore + f5 * scaleCore), x0, x2);
        tessellator.addVertexWithUV((posX + f1 * scaleCore - f4 * scaleCore), (posY - f2 * scaleCore),
                (posZ + f3 * scaleCore - f5 * scaleCore), x0, x3);
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        (FMLClientHandler.instance().getClient()).renderEngine.bindTexture(particleTexture);
        int qq = this.ticker % 16;
        i = 24 + qq;
        float size8 = (size2 * 8);
        float_sizeMinus0_01 = size2 - 0.01F;
        x0 = ((i % 8 * size2) + 0.0F) / size8;
        x1 = ((i % 8 * size2) + float_sizeMinus0_01) / size8;
        x2 = ((i / 8 * size2) + 0.0F) / size8;
        x3 = ((i / 8 * size2) + float_sizeMinus0_01) / size8;
        float var11 = MathHelper.sin(this.ticker / 10.0F) * 0.1F;
        scaleCore = 0.4F + var11;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(240);
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.addVertexWithUV((posX - f1 * scaleCore - f4 * scaleCore), (posY - f2 * scaleCore),
                (posZ - f3 * scaleCore - f5 * scaleCore), x1, x3);
        tessellator.addVertexWithUV((posX - f1 * scaleCore + f4 * scaleCore), (posY + f2 * scaleCore),
                (posZ - f3 * scaleCore + f5 * scaleCore), x1, x2);
        tessellator.addVertexWithUV((posX + f1 * scaleCore + f4 * scaleCore), (posY + f2 * scaleCore),
                (posZ + f3 * scaleCore + f5 * scaleCore), x0, x2);
        tessellator.addVertexWithUV((posX + f1 * scaleCore - f4 * scaleCore), (posY - f2 * scaleCore),
                (posZ + f3 * scaleCore - f5 * scaleCore), x0, x3);
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {
        renderTileEntityAt((TileEntityMolecularTransformer) te, x, y, z, scale);
    }

    public void renderTileEntityAt(TileEntityMolecularTransformer tileTransformer, double x, double y, double z,
                                   float scale) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        (FMLClientHandler.instance().getClient()).renderEngine.bindTexture(transformerTexture);
        this.model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        if (tileTransformer.getActive()) {
            GL11.glPushMatrix();
            renderCore(tileTransformer, x, y, z, scale);
            GL11.glPopMatrix();
        }
    }
}
