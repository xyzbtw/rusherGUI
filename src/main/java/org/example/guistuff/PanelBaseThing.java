package org.example.guistuff;

import org.example.ExamplePlugin;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.render.IRenderer2D;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.ui.panel.PanelBase;
import org.rusherhack.client.api.ui.panel.PanelHandlerBase;

import java.awt.*;

public class PanelBaseThing extends PanelBase<PanelItemThing> {
    public PanelBaseThing() {
        super((PanelHandlerBase<? extends PanelBase<PanelItemThing>>) RusherHackAPI.getThemeManager().getClickGuiHandler(), "hi");
    }

    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        IRenderer2D renderer = ExamplePlugin.theme.getClickGuiHandler().getRenderer();
        renderer.begin(context.pose());

        renderer.drawRectangle(50, 1000, 1000, 1000, Color.BLACK.getRGB());
        renderer.getFontRenderer().drawString("panelbasething", 100, 1000, Color.RED.getRGB());

        renderer.end();
    }

    @Override
    public double getWidth() {
        return 100;
    }

    @Override
    public double getHeight() {
        return 100;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

    @Override
    public boolean isHovered(double mouseX, double mouseY) {
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
