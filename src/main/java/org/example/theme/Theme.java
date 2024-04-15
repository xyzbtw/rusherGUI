package org.example.theme;

import org.jetbrains.annotations.Nullable;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.client.api.ui.panel.PanelHandlerBase;
import org.rusherhack.client.api.ui.theme.ThemeBase;
import org.rusherhack.core.setting.NumberSetting;

import java.awt.*;

public class Theme extends ThemeBase {
    public ColorSetting fontColor = new ColorSetting("FontColor", new Color(255, 255, 255));
    public NumberSetting<Float> hoverAlpha = new NumberSetting<>("HoverAlpha", 127F, 0F, 255F);
    public ColorSetting backColor = new ColorSetting("BackColor", new Color(0, 0, 0, 150));
    public ColorSetting outlineColor = new ColorSetting("OutlineColor", new Color(145, 145, 145, 100));
    public ColorSetting moduleOutlineColor = new ColorSetting("ModuleOutlineColor", new Color(145, 145, 145, 100));
    public NumberSetting<Float> outlineWidth = new NumberSetting<>("OutlineWidth", 3F, 0.1F, 5F);
    public NumberSetting<Float> scrollSpeed = new NumberSetting<>("ScrollSpeed", 15F, 1F, 20F);

    public Theme(String name, String description, Color defaultColor) {
        super(name, description, defaultColor);
        getColorSetting().setValue(new Color(112, 104, 255));
        registerSettings(
                fontColor,
                hoverAlpha,
                backColor,
                outlineColor,
                moduleOutlineColor,
                outlineWidth,
                scrollSpeed
        );
    }

    @Override
    public @Nullable PanelHandlerBase<?> getClickGuiHandler() {
        return ExamplePlugin.handler;
    }

}
