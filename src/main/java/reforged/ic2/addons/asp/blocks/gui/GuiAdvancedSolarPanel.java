package reforged.ic2.addons.asp.blocks.gui;

import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import reforged.ic2.addons.asp.blocks.container.ContainerAdvancedSolarPanel;
import reforged.ic2.addons.asp.tiles.TileEntityAdvancedSolarPanel;
import reforged.ic2.addons.asp.utils.EnergyUtils;

public class GuiAdvancedSolarPanel extends GuiContainer {

    TileEntityAdvancedSolarPanel tile;
    String texture = "/mods/AdvancedSolarPanels/textures/gui/solar_panel.png";

    public GuiAdvancedSolarPanel(InventoryPlayer inventoryPlayer, TileEntityAdvancedSolarPanel tile) {
        super(new ContainerAdvancedSolarPanel(inventoryPlayer, tile));
        this.tile = tile;
        this.allowUserInput = false;
        this.xSize = 194;
        this.ySize = 168;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        // Center the title
        String name = tile.getInvName();
        int nameX = (this.xSize - this.fontRenderer.getStringWidth(name)) / 2;
        this.fontRenderer.drawString(name, nameX, 7, 0x757575); // 7718655 in hex

        int production = tile.isSunVisible() ? tile.dayGen : tile.nightGen;
        // Draw energy info
        this.fontRenderer.drawString(FormattedTranslator.WHITE.format("message.info.solar.storage", EnergyUtils.formatInt(tile.storage, 4), EnergyUtils.formatInt(tile.maxStorage, 0)), 50, 22, 0);
        this.fontRenderer.drawString(FormattedTranslator.WHITE.format("message.info.solar.max.out", tile.maxOutput), 50, 32, 0);
        this.fontRenderer.drawString(FormattedTranslator.WHITE.format("message.info.solar.generating", production), 50, 42, 0);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // Reset color to full white (no tint)
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);

        // GUI top-left corner
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        // Draw main background
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

        // Draw energy bar if there's any energy stored
        if (this.tile.storage > 0) {
            int energyBarWidth = this.tile.gaugeStorageScaled(24);
            drawTexturedModalRect(x + 19, y + 24, 195, 0, energyBarWidth + 1, 14);
        }

        // Draw sun indicator if the sky is visible
        if (this.tile.generationState != TileEntityAdvancedSolarPanel.GenerationState.NONE) {
            int u = this.tile.generationState == TileEntityAdvancedSolarPanel.GenerationState.DAY ? 195 : 210;
            drawTexturedModalRect(x + 24, y + 42, u, 15, 14, 14);
        }
    }
}
