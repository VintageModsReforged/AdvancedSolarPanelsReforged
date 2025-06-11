package reforged.ic2.addons.asp.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class ExtendedButton extends GuiButton {

    public ExtendedButton(int id, int xPos, int yPos, int width, int height, String displayString) {
        super(id, xPos, yPos, width, height, displayString);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!this.drawButton) return;
        // Hover check
        boolean hovered = mouseX >= this.xPosition && mouseY >= this.yPosition &&
                mouseX < this.xPosition + this.width &&
                mouseY < this.yPosition + this.height;
        mc.renderEngine.bindTexture("/gui/gui.png");

        // Set color to white (normal)
        GL11.glColor4f(1F, 1F, 1F, 1F);

        // Get hover state (0 = normal, 1 = hovered, 2 = disabled)
        int hoverState = this.getHoverState(hovered);

        drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + hoverState * 20, this.width / 2, this.height / 2);
        drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + hoverState * 20, this.width / 2, this.height / 2);
        drawTexturedModalRect(this.xPosition, this.yPosition + this.height / 2, 0, 66 + hoverState * 20 - this.height / 2, this.width / 2, this.height / 2);
        drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition + this.height / 2, 200 - this.width / 2, 66 + hoverState * 20 - this.height / 2, this.width / 2, this.height / 2);

        int textColor = 14737632;
        if (!this.enabled) {
            textColor = -6250336;
        } else if (hovered) {
            textColor = 16777120;
        }

        String display = this.displayString;
        int textWidth = mc.fontRenderer.getStringWidth(display);
        int maxWidth = this.width - 6;
        if (textWidth > maxWidth) {
            String trimmed = mc.fontRenderer.trimStringToWidth(display, maxWidth - mc.fontRenderer.getStringWidth("..."));
            display = trimmed + "...";
        }

        // Draw centered text
        drawCenteredString(mc.fontRenderer, display, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, textColor);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
