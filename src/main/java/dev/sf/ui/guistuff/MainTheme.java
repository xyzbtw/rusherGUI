package dev.sf.ui.guistuff;

import org.jetbrains.annotations.Nullable;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.client.api.ui.panel.PanelHandlerBase;
import org.rusherhack.client.api.ui.theme.ITheme;
import org.rusherhack.core.setting.NumberSetting;
import org.rusherhack.core.setting.Setting;
import org.rusherhack.core.setting.StringSetting;

import java.awt.*;
import java.util.List;

public class MainTheme implements ITheme {


    public @Nullable PanelHandlerBase<?> getClickGuiHandler() {
        return new PanelThing(true);
    }
    public ColorSetting colorSetting = new ColorSetting("Color Setting", new Color(0, 0, 0, 0));
    public Setting<String> stringSetting = new StringSetting("String Setting", "default");
    public Setting<Integer> integerSetting = new NumberSetting<>("Integer Setting", "das", 5, 0, 5);

    @Override
    public ColorSetting getColorSetting() {
        return colorSetting;
    }

    @Override
    public List<Setting<?>> getSettings() {
        return List.of(
                stringSetting,
                colorSetting,
                integerSetting
        );
    }

    @Override
    public String getDescription() {
        return "thisad";
    }

    @Override
    public String getName() {
        return "xyzthemething";
    }
}
