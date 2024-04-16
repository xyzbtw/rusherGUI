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
    public BooleanItem(IModule module, Panel panel, ExtendableItem parent, BooleanSetting setting) {
        super(parent, module, panel, setting);
        this.panel = panel;
        this.module = module;
        this.setting = setting;
    }
    Panel panel;
    IModule module;
    BooleanSetting setting;



    @Override
    public boolean isHovered(double mouseX, double mouseY, boolean includeSubItems) {
        return false;
    }


    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        super.render(context, mouseX, mouseY);
        open = setting.getValue();
        IRenderer2D renderer = RusherHackAPI.getRenderer2D();

        renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(), setting.getValue()
                ? ExamplePlugin.theme.getColorSetting().getValue().getRGB()
                : new Color(0, 0, 0, 50).getRGB());

        if(panel.isHovering(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
            renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(), new Color(0, 0, 0, 70).getRGB());
        }

        renderSubItems(context, mouseX, mouseY, subItems, open);

        fontRenderer.drawText(fontRenderer.trimStringToWidth(setting.getName(), getWidth()), getX() + 1, getY() + 2.5, ExamplePlugin.theme.fontColor.getValueRGB(), getWidth(), 1);

    }
    @Override
    public double getX() {
        return parent.getX() + 1.5;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0){
            if(panel.isHovering(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
                System.out.println("PRE " + setting.getValue());
                setting.setValue(!setting.getValue());
                System.out.println("POST " + setting.getValue());
            }
        }

        return false;
    }

    @Override
    public double getY() {
        return super.getY();
    }

    @Override
    public double getWidth() {
        return super.getWidth();
    }

    @Override
    public double getHeight() {
        return super.getHeight();
    }

}
