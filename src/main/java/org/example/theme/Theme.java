package org.example.theme;

import org.jetbrains.annotations.Nullable;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.client.api.ui.panel.PanelHandlerBase;
import org.rusherhack.client.api.ui.theme.ThemeBase;
import org.rusherhack.core.setting.NumberSetting;
import org.rusherhack.core.setting.Setting;

import java.awt.Color;
import java.lang.reflect.Field;


public class Theme extends ThemeBase {
    public ColorSetting fontColor = new ColorSetting("FontColor", new Color(148, 141, 252));
    public NumberSetting<Float> hoverAlpha = new NumberSetting<>("HoverAlpha", 127F, 0F, 255F);
    public ColorSetting backColor = new ColorSetting("BackColor", new Color(0, 0, 0, 100));
    public ColorSetting outlineColor = new ColorSetting("OutlineColor", new Color(100, 0, 0, 100));
    public NumberSetting<Float> outlineWidth = new NumberSetting<>("OutlineWidth", 1F, 0.1F, 5F);
    public NumberSetting<Float> scrollSpeed = new NumberSetting<>("ScrollSpeed", 15F, 1F, 20F);

    public Theme(String name, String description, Color defaultColor) {
        super(name, description, defaultColor);
        Setting<?>[] settings = new Setting[420];
        int index = 0;
        try {
            Class<?> clazz = getClass();
            for (Field field : clazz.getDeclaredFields()) {
                if (Setting.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    settings[index++] = (Setting<?>) field.get(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerSettings(settings);
    }

    @Override
    public @Nullable PanelHandlerBase<?> getClickGuiHandler() {
        registerSettings();
        return ExamplePlugin.handler;
    }

}
