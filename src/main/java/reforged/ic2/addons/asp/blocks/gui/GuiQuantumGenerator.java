package reforged.ic2.addons.asp.blocks.gui;

import ic2.api.network.NetworkHelper;
import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.opengl.GL11;
import reforged.ic2.addons.asp.blocks.container.ContainerQuantumGenerator;
import reforged.ic2.addons.asp.tiles.TileEntityQuantumGenerator;
import reforged.ic2.addons.asp.utils.ExtendedButton;

import java.util.ArrayList;
import java.util.List;

public class GuiQuantumGenerator extends GuiContainer {

    public static final String[] TIERS = new String[]{"UV", "LV", "MV", "HV", "EV", "IV", "LuV"};

    ContainerQuantumGenerator container;
    TileEntityQuantumGenerator tile;;
    String texture = "/mods/AdvancedSolarPanels/textures/gui/quantum_generator.png";

    public GuiQuantumGenerator(ContainerQuantumGenerator container) {
        super(container);
        this.container = container;
        this.tile = container.tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        // Reset color to full white (no tint)
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);

        // GUI top-left corner
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        // Draw main background
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        String name = tile.getInvName();

        int nameX = (this.xSize - this.fontRenderer.getStringWidth(name)) / 2;
        this.fontRenderer.drawString(name, nameX, 7, 0x757575); // 7718655 in hex
        String text = FormattedTranslator.WHITE.format("message.info.quantum.packet", tile.packets);
        int posX = (this.xSize - this.fontRenderer.getStringWidth(text)) / 2;
        this.fontRenderer.drawString(text, posX, 20, 4210752);
        text = FormattedTranslator.WHITE.format("message.info.quantum.packet.size", tile.packetEnergy);
        posX = (this.xSize - this.fontRenderer.getStringWidth(text)) / 2;
        this.fontRenderer.drawString(text, posX, 37, 4210752);
        text = FormattedTranslator.WHITE.format("message.info.quantum.energy.total", tile.packets * tile.packetEnergy);
        posX = (this.xSize - this.fontRenderer.getStringWidth(text)) / 2;
        this.fontRenderer.drawString(text, posX, 70, 4210752);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        List<String> tooltip = new ArrayList<String>();
        for (Object obj : this.buttonList) {
            GuiButton btn = (GuiButton) obj;
            if (btn instanceof ExtendedButton) {
                ExtendedButton myButton = (ExtendedButton) btn; // my custom button instance
                if (myButton.id == 0 || myButton.id == 2 || myButton.id == 1 || myButton.id == 3) {
                    boolean hover = mouseX >= myButton.xPosition && mouseY >= myButton.yPosition &&
                            mouseX < myButton.xPosition + myButton.getWidth() &&
                            mouseY < myButton.yPosition + myButton.getHeight();
                    if (hover) {
                        if (btn.id == 0 || btn.id == 2) {
                            tooltip.add(FormattedTranslator.WHITE.format("message.info.quantum.info.1"));
                        } else {
                            tooltip.add(FormattedTranslator.WHITE.format("message.info.quantum.info.1"));
                            tooltip.add(FormattedTranslator.WHITE.format("message.info.quantum.info.2"));
                            tooltip.add(FormattedTranslator.WHITE.format("message.info.quantum.info.3"));
                        }
                    }
                }
            }
        }

        if (!tooltip.isEmpty()) {
            drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        // GUI top-left corner
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.container.packets = this.tile.packets;
        this.container.packetEnergy = this.tile.packetEnergy;

        this.buttonList.add(new ExtendedButton(0, x + 157, y + 18, 12, 12, "+")); // packet +
        this.buttonList.add(new ExtendedButton(1, x + 157, y + 35, 12, 12, "+")); // packetEnergy +
        this.buttonList.add(new ExtendedButton(2, x + 7, y + 18, 12, 12, "-")); // packet -
        this.buttonList.add(new ExtendedButton(3, x + 7, y + 35, 12, 12, "-")); // packetEnergy -

        int xOffset = 13;
        for (int i = 0; i < TIERS.length; i++) {
            String tier = TIERS[i];
            int offset = fontRenderer.getStringWidth(tier) + 6;
            this.buttonList.add(new ExtendedButton(1000 + i, x + xOffset, y + 55, offset, 12, tier));
            xOffset += offset + 3;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        // packets
        if (button.id == 0) changePacket(1);
        if (button.id == 2) changePacket(-1);
        // packetEnergy
        if (button.id == 1) changePacketEnergy(1);
        if (button.id == 3) changePacketEnergy(-1);
        // tier
        if (button.id >= 1000) changeTier(button.id - 1000);
        super.actionPerformed(button);
    }

    public void changePacket(int mul) {
        int key = isCtrlKeyDown() ? 11 : 1; // -1 and -11
        // network
        // -1 and -11 vs 1 and 11
        // meaning -1 and -10 vs +1 and +10 when changing packets
        NetworkHelper.initiateClientTileEntityEvent(this.tile, mul * key);
    }

    public void changePacketEnergy(int mul) {
        int key = isCtrlKeyDown() && isShiftKeyDown() ? 2222 // -2222
                : isShiftKeyDown() ? 222 // -222
                : isCtrlKeyDown() ? 22 // -22
                : 2; // -2
        // -2 and -22 and -222 vs 2 and 22 and 222
        // same as packets when changing packetEnergy
        // network
        NetworkHelper.initiateClientTileEntityEvent(this.tile, mul * key);
    }

    public void changeTier(int tier) {
        NetworkHelper.initiateClientTileEntityEvent(this.tile, 1000 + tier);
    }
}
