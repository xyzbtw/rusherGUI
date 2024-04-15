package org.example.theme.item.items;

import net.minecraft.ChatFormatting;
import org.example.theme.ExamplePlugin;
import org.example.theme.Panel;
import org.rusherhack.client.api.IRusherHack;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.core.bind.key.IKey;
import org.rusherhack.core.setting.Setting;
import org.rusherhack.core.utils.Timer;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UNKNOWN;

public class BindItem extends ExtendableItem{
    public boolean isListening = false;
    private final Timer idleTimer = new Timer();
    private int idling = 0;
    private static final String[] strings = new String[]{"", ".", "..", "..."};

    public BindItem(ModuleItem parent, IModule module, Panel panel, Setting<?> settingValue) {
        super(parent, module, panel, settingValue);
    }
    @Override
    public double getX() {
        return super.getX() + 1.5;
    }

    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        System.out.println("rendering bindthign");

        renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(), ExamplePlugin.theme.getColorSetting().getValueRGB());
        if(isHovered(mouseX, mouseY)) {
            renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(), new Color(0,0,0, ExamplePlugin.theme.hoverAlpha.getValue()).getRGB());
        }

        if (isListening) {
            fontRenderer.drawString(setting.getName() + ": Waiting" + getIdleSign(), getX() + 1, getY() + 1, ExamplePlugin.theme.fontColor.getValueRGB());
        } else {
            fontRenderer.drawString(setting.getName() + ": " + (setting.getDisplayValue().equalsIgnoreCase("unknown") ? "NONE" : setting.getDisplayValue()), getX() + 1, getY() + 1, ExamplePlugin.theme.fontColor.getValueRGB());
        }
        if (isHovering(mouseX, mouseY)) {
            String description =
                    ChatFormatting.GREEN +
                            "Value " +
                            ChatFormatting.RESET +
                            "«" + (isListening ? "Waiting" + getIdleSign() : setting.getValue().toString()) + "»." +
                            "\n" +
                            ChatFormatting.RESET +
                            (setting.getDescription().isEmpty() ?
                                    "A Bind setting." + ChatFormatting.GREEN + " Name" + ChatFormatting.RESET + " «" + setting.getName() + "»."
                                    : setting.getDescription());

            drawDesc(renderer, mouseX + 8, mouseY + 8, description);
        }


    }

    @Override
    public boolean keyTyped(int key, int scanCode, int modifiers) {
        if (isListening) {
            IKey bind = RusherHackAPI.getBindManager().createKeyboardKey(key);
            if (key == GLFW_KEY_ESCAPE
                    || key == GLFW_KEY_DELETE
                    || key == GLFW_KEY_BACKSPACE

            ) {
                bind = RusherHackAPI.getBindManager().createKeyboardKey(GLFW_KEY_UNKNOWN);
            }
            setting.setValue(bind);
            isListening = false;
        }
        return super.keyTyped(key, scanCode, modifiers);
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isHovering(mouseX, mouseY)) {
            return false;
        }

        if (isListening) {
            setting.setValue(RusherHackAPI.getBindManager().createMouseKey(button));
            isListening = false;
        } else {
            if (button == GLFW_MOUSE_BUTTON_LEFT) {
                isListening = !isListening;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public String getIdleSign() {
        if (idleTimer.passed(500L)) {
            idleTimer.reset();
            idling++;
            if (idling > 3) idling = 0;

        }
        return strings[idling];
    }
}
