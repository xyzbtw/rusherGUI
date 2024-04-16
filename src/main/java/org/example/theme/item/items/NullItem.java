package org.example.theme.item.items;

import org.example.theme.ExamplePlugin;
import org.example.theme.Panel;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.core.setting.NullSetting;
import org.rusherhack.core.setting.Setting;

public class NullItem extends ExtendableItem{
    public NullItem(ExtendableItem parent, IModule module, Panel panel, Setting<?> settingValue) {
        super(parent, module, panel, settingValue);
    }


    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        super.render(context, mouseX, mouseY);
        renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(), ExamplePlugin.theme.getColorSetting().getValueRGB());

        fontRenderer.drawText(fontRenderer.trimStringToWidth(setting.getDisplayName(), getWidth()), getX() + 1, getY() + 1, ExamplePlugin.theme.fontColor.getValueRGB(), getWidth(), 1.0);
        fontRenderer.drawText("\u2022\u2022\u2022", getX() + getWidth() - fontRenderer.getStringWidth("\u2022\u2022\u2022") - 3, getY() + 1.5, ExamplePlugin.theme.fontColor.getValueRGB(), getWidth(), 1.0);

        renderSubItems(context, mouseX, mouseY, subItems, open);

        super.render(context, mouseX, mouseY);
    }
    @Override
    public double getX() {
        return parent.getX() + 1.5;
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



    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(isHovering(mouseX, mouseY) && button == 1) {
            open = !open;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }



}
