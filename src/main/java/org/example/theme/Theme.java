package org.example.theme;

import org.jetbrains.annotations.Nullable;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.client.api.ui.panel.PanelHandlerBase;
import org.rusherhack.client.api.ui.theme.ITheme;
import org.rusherhack.core.setting.Setting;

import java.awt.*;
import java.util.List;

public class Theme implements ITheme {
    public ColorSetting colorSetting = new ColorSetting("TestColor", new Color(148, 141, 252));
    public ColorSetting fontColor = new ColorSetting("FontColor", new Color(148, 141, 252));

    @Override
    public @Nullable PanelHandlerBase<?> getClickGuiHandler() {
        return new ClickGUIHandler(true);
    }

    @Override
    public ColorSetting getColorSetting() {
        return colorSetting;
    }

    @Override
    public List<Setting<?>> getSettings() {
        return List.of(
                colorSetting,
                fontColor
        );
    }

    @Override
    public String getDescription() {
        return "Funny theme";
    }

    @Override
    public String getName() {
        return "TestTHEME";
    }
}
