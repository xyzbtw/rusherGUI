package org.example.theme;

import org.jetbrains.annotations.Nullable;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.client.api.ui.panel.PanelHandlerBase;
import org.rusherhack.client.api.ui.theme.ThemeBase;

import java.awt.*;

public class Theme extends ThemeBase {
    public ColorSetting fontColor = new ColorSetting("FontColor", new Color(148, 141, 252));
    public ColorSetting backColor = new ColorSetting("BackColor", new Color(0, 0, 0, 100));
    public ColorSetting outlineColor = new ColorSetting("OutlineColor", new Color(100, 0, 0, 100));

    public Theme(String name, String description, Color defaultColor) {
        super(name, description, defaultColor);
        registerSettings(
                backColor,
                outlineColor,
                fontColor
        );
    }

    @Override
    public @Nullable PanelHandlerBase<?> getClickGuiHandler() {
        registerSettings();
        return ExamplePlugin.handler;
    }

}
