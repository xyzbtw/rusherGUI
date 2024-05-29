package org.example.theme.item.items;

import org.example.theme.ExamplePlugin;
import org.example.theme.Panel;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.render.IRenderer2D;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.core.setting.BooleanSetting;

import java.awt.*;

public class BooleanItem extends ExtendableItem {
    public BooleanItem(IModule module, Panel panel, ExtendableItem parent, BooleanSetting setting) {
        super(parent, module, panel, setting);
        this.panel = panel;
        this.module = module;
        this.setting = setting;
        open = false;
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
        IRenderer2D renderer = RusherHackAPI.getRenderer2D();

        renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(), setting.getValue()
                ? ExamplePlugin.theme.getColorSetting().getValue().getRGB()
                : new Color(0, 0, 0, 50).getRGB());

        if(panel.isHovering(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
            renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(), new Color(0, 0, 0, 70).getRGB());
        }

        renderSubItems(context, mouseX, mouseY, subItems, open);

        fontRenderer.drawText(fontRenderer.trimStringToWidth(setting.getDisplayName(), getWidth()), getX() + 1, getY() + 1.5, ExamplePlugin.theme.fontColor.getValueRGB(), getWidth(), 1);

        if(!subItems.isEmpty()){
            fontRenderer.drawText(get3Dots(), getX() + getWidth() - fontRenderer.getStringWidth(get3Dots()) - 3, getY() + 1.5, ExamplePlugin.theme.fontColor.getValueRGB(), getWidth(), 1.0);
        }
    }
    @Override
    public double getX() {
        return parent.getX() + 1.5;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0 && parent.open){
            if(panel.isHovering(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
                setting.setValue(!setting.getValue());
            }
        }
        if(button == 1 && parent.open && panel.isHovering(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
            open = !open;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public double getY() {
        return super.getY();
    }

    @Override
    public double getHeight() {
        return super.getHeight();
    }

}
