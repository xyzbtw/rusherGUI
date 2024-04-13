package org.example.guistuff;

import org.jetbrains.annotations.Nullable;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.client.api.ui.panel.PanelHandlerBase;
import org.rusherhack.client.api.ui.theme.ITheme;
import org.rusherhack.core.setting.Setting;

import java.util.List;

public class MainTheme implements ITheme {


    public @Nullable PanelHandlerBase<?> getClickGuiHandler() {
        return new PanelThing(true);
    }

    @Override
    public ColorSetting getColorSetting() {
        return null;
    }

    @Override
    public List<Setting<?>> getSettings() {
        return List.of();
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }
}
