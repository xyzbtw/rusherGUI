package org.example.theme.item.items;

import net.minecraft.ChatFormatting;
import org.example.theme.ExamplePlugin;
import org.example.theme.Panel;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.render.IRenderer2D;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.core.setting.Setting;
import org.rusherhack.core.setting.StringSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class EnumItem extends ExtendableItem{
    private boolean open = false;


    public EnumItem(ExtendableItem parent, IModule module, Panel panel, Setting<?> settingValue) {
        super(parent, module, panel, settingValue);
    }


    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        super.render(context, mouseX, mouseY);
        renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(), ExamplePlugin.theme.getColorSetting().getValueRGB());
        if(isHovering(mouseX, mouseY)) {
            renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(), new Color(0,0,0, 70).getRGB());
        }

        fontRenderer.drawText(fontRenderer.trimStringToWidth(setting.getName() + ": " + setting.getValue(), getWidth()), getX() + 1, getY() + 1.5, ExamplePlugin.theme.fontColor.getValueRGB(), getWidth(), 1);

        renderSubItems(context, mouseX, mouseY, subItems, open);

        if (isHovering(mouseX, mouseY)) {
            String description =
                    ChatFormatting.GREEN +
                            "Value " +
                            ChatFormatting.RESET +
                            "«" + setting.getValue() + "»." +
                            "\n" +
                            ChatFormatting.RESET +
                            (setting.getDescription().isEmpty() ?
                                    "A Mode setting." + ChatFormatting.GREEN + " Name" + ChatFormatting.RESET + " «" + setting.getName() + "»."
                                    : setting.getDescription());

            drawDesc(renderer, mouseX + 8, mouseY + 8, description);
        }
    }
    @Override
    public double getX() {
        return parent.getX() + 1.5;
    }

    public void next() {
        if(setting instanceof StringSetting && !((StringSetting) setting).getOptions().isEmpty()) {
            LinkedHashSet<String> options = ((StringSetting) setting).getOptions();
            String currentValue = (String) setting.getValue();
            int currentIndex = new ArrayList<>(options).indexOf(currentValue);
            currentIndex = (currentIndex + 1) % options.size();
            setting.setValue(new ArrayList<>(options).get(currentIndex));
        } else if (this.setting.getValue() instanceof Enum<?> value) {

            Enum<?>[] array = value.getDeclaringClass().getEnumConstants();
            this.setting.setValue((array.length - 1 == value.ordinal()
                    ? array[0]
                    : array[value.ordinal() + 1]));

        } else {
            throw new IllegalStateException("value isn't an enum or a StringSetting with options");
        }
    }
    public void previous() {
        if(setting instanceof StringSetting && !((StringSetting) setting).getOptions().isEmpty()) {

            List<String> options = new ArrayList<>(((StringSetting) setting).getOptions());
            String currentValue = (String) setting.getValue();
            int currentIndex = options.indexOf(currentValue);
            currentIndex = (currentIndex - 1 + options.size()) % options.size();
            setting.setValue(options.get(currentIndex));

        } else if (this.setting.getValue() instanceof Enum<?> value) {

            Enum<?>[] array = value.getDeclaringClass().getEnumConstants();
            this.setting.setValue((value.ordinal() - 1 < 0
                    ? array[array.length - 1]
                    : array[value.ordinal() - 1]));

        } else {
            throw new IllegalStateException("value isn't an enum or a StringSetting with options");
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        switch (button) {
            case GLFW_MOUSE_BUTTON_LEFT -> {
                if (isHovering(mouseX, mouseY)) {
                    next();
                }
            }
            case GLFW_MOUSE_BUTTON_RIGHT -> {
                if (isHovering(mouseX, mouseY)) {
                    previous();
                }
            }
            case GLFW_MOUSE_BUTTON_MIDDLE -> {
                if( !isHovering(mouseX, mouseY)) return false;
                open = !open;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char character) {
        return super.charTyped(character);
    }

    @Override
    public boolean keyTyped(int key, int scanCode, int modifiers) {
        return super.keyTyped(key, scanCode, modifiers);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
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
