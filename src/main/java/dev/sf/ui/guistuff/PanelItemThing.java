package dev.sf.ui.guistuff;

import dev.sf.ui.ExamplePlugin;
import org.rusherhack.client.api.render.IRenderer2D;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.ui.panel.IPanelItem;

import java.awt.*;

public class PanelItemThing extends ElementThing implements IPanelItem {
    @Override
    public double getWidth() {
        return 100;
    }

    @Override
    public double getHeight(boolean total) {
        return 1000;
    }

    @Override
    public boolean isHovered(double mouseX, double mouseY, boolean includeSubItems) {
        return false;
    }

    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        IRenderer2D renderer = ExamplePlugin.theme.getClickGuiHandler().getRenderer();
        renderer.begin(context.pose());

        renderer.drawRectangle(50, 1000, 1000, 1000, Color.BLACK.getRGB());
        renderer.getFontRenderer().drawString("PANELITEMTHING", 50, 1000, Color.RED.getRGB());

        renderer.end();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean charTyped(char character) {
        return false;
    }

    @Override
    public boolean keyTyped(int key, int scanCode, int modifiers) {
        return false;
    }
}
