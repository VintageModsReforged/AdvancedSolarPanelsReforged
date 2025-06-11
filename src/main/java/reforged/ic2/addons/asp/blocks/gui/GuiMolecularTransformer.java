package reforged.ic2.addons.asp.blocks.gui;

import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import reforged.ic2.addons.asp.blocks.container.ContainerMolecularTransformer;
import reforged.ic2.addons.asp.tiles.TileEntityMolecularTransformer;
import reforged.ic2.addons.asp.utils.molecular.MTRecipeManager;
import reforged.ic2.addons.asp.utils.molecular.RecipeRecord;

public class GuiMolecularTransformer extends GuiContainer {

    TileEntityMolecularTransformer tile;
    String texture = "/mods/AdvancedSolarPanels/textures/gui/molecular_transformer.png";

    public GuiMolecularTransformer(InventoryPlayer inventoryPlayer, TileEntityMolecularTransformer tile) {
        super(new ContainerMolecularTransformer(inventoryPlayer, tile));
        this.tile = tile;
        this.xSize = 220;
        this.ySize = 193;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);

        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        // Enable blending for semi-transparent effects
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Draw background texture
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);

        GL11.glDisable(GL11.GL_BLEND);

        // Draw progress bar if active
        if (this.tile.lastProgress > 0) {
            int progress = this.tile.lastProgress * 15 / 100;
            drawTexturedModalRect(guiLeft + 23, guiTop + 48, 221, 7, 10, progress + 1);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        int yOffest = 3;
        int fontHeight = fontRenderer.FONT_HEIGHT;
        int offset = yOffest + fontHeight;
        String invName = this.tile.getInvName();
        int nmPos = (this.xSize - this.fontRenderer.getStringWidth(invName)) / 2;
        this.fontRenderer.drawString(invName, nmPos, 8, 0);
        int lastRecipe = this.tile.lastRecipeNumber;
        if (lastRecipe != -1) {
            RecipeRecord recipe = MTRecipeManager.instance.getRecipes().get(lastRecipe);
            String text = FormattedTranslator.WHITE.format("message.info.molecular.input", recipe.input.stackSize + "*" + recipe.input.getDisplayName());
            this.fontRenderer.drawString(text, 56, 26, 0);
            text = FormattedTranslator.WHITE.format("message.info.molecular.output", recipe.output.stackSize + "*" + recipe.output.getDisplayName());
            this.fontRenderer.drawString(text, 56, 26 + offset, 0);
            text = FormattedTranslator.WHITE.format("message.info.molecular.energy", recipe.energy);
            this.fontRenderer.drawString(text, 56, 26 + offset * 2, 0);
            text = FormattedTranslator.WHITE.format("message.info.molecular.energy.in", this.tile.inputEU);
            this.fontRenderer.drawString(text, 56, 26 + offset * 3, 0);
            text = FormattedTranslator.WHITE.format("message.info.molecular.progress", this.tile.lastProgress);
            this.fontRenderer.drawString(text, 56, 26 + offset * 4, 0);
        }
    }
}
