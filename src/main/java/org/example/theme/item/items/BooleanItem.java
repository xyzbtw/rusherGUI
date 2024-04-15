package org.example.theme.item.items;

import org.example.theme.ExamplePlugin;
import org.example.theme.Panel;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.render.IRenderer2D;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.ui.ElementBase;
import org.rusherhack.client.api.ui.panel.IPanelItem;
import org.rusherhack.core.setting.BooleanSetting;

import java.awt.*;
import java.util.ArrayList;

public class BooleanItem extends ExtendableItem {
    public BooleanItem(IModule module, Panel panel, ModuleItem parent, BooleanSetting setting) {
        super(parent);
        this.parent = parent;
        this.module = module;
        this.panel = panel;
        this.setting = setting;
    }
    Panel panel;
    IModule module;
    ModuleItem parent;
    private final ArrayList<IPanelItem> subItems = new ArrayList<>();
    private double prevHeight, rendererHeight;
    BooleanSetting setting;
    @Override
    public double getWidth() {
        return parent.getWidth() - 3;
    }

    @Override
    public double getHeight(boolean total) {
        return 11;
    }

    @Override
    public boolean isHovered(double mouseX, double mouseY, boolean includeSubItems) {
        return false;
    }

    @Override
    public double getHeight() {
        return 11;
    }

    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        IRenderer2D renderer = RusherHackAPI.getRenderer2D();

        renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(), setting.getValue()
                ? ExamplePlugin.theme.getColorSetting().getValue().getRGB()
                : new Color(0, 0, 0, 50).getRGB());

        renderer.getFontRenderer().drawString(setting.getName(), getX() + 1, getY(), ExamplePlugin.theme.fontColor.getValueRGB());

        if(panel.isHovering(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
            renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(), new Color(0, 0, 0, 100).getRGB());
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0){
            if(panel.isHovering(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
                setting.setValue(!setting.getValue());
            }
        }
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
