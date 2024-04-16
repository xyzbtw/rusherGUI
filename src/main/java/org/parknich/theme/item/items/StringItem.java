package org.parknich.theme.item.items;

import net.minecraft.ChatFormatting;
import org.parknich.theme.ParkTheme;
import org.parknich.theme.Panel;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.core.setting.Setting;
import org.rusherhack.core.utils.Timer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.rusherhack.client.api.Globals.mc;


public class StringItem extends ExtendableItem{

    private int count;
    private int index = 1;
    private final Timer idleTimer = new Timer();
    private boolean idling = false, listening = false, isIndex = false;
    private StringBuilder str = new StringBuilder();
    private final List<StringBuilder> ctrlz = new LinkedList<>();

    public StringItem(ExtendableItem parent, IModule module, Panel panel, Setting<?> setting) {
        super(parent, module, panel, setting);
        ctrlz.add(new StringBuilder((String) (Objects.equals(setting.getValue(), "") ? setting.getDefaultValue() : setting.getValue())));
        ctrlz.add(str);
        count = ctrlz.size() - 1;
    }


    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        super.render(context, mouseX, mouseY);
        renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(false), ParkTheme.theme.getColorSetting().getValueRGB());
        if(isHovering(mouseX, mouseY)) {
            renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(false), new Color(0,0,0, 70).getRGB());
        }


        fontRenderer.drawText(fontRenderer.trimStringToWidth(setting.getDisplayName() + ": " + (listening ? str.toString() : setting.getValue()), getWidth()), getX() + 1, getY() + 1, ParkTheme.theme.fontColor.getValueRGB(), getWidth(), 1);

        if (listening) {
            fontRenderer.drawText(getIdleSign(),
            fontRenderer.getStringWidth(setting.getDisplayName() + ": " + (listening ? str.toString() : setting.getValue())),
                    getY() + 1,
                    ParkTheme.theme.fontColor.getValueRGB(), getWidth(), 1);
        }

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
                                    "A String setting." + ChatFormatting.GREEN + " Name" + ChatFormatting.RESET + " «" + setting.getDisplayName() + "»."
                                    : setting.getDescription());

            drawDesc(renderer, mouseX + 8, mouseY + 8, description);
        }
    }
    @Override
    public double getX() {
        return parent.getX() + 1.5;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovering(mouseX, mouseY)) {
            if (button == GLFW_MOUSE_BUTTON_LEFT || button == GLFW_MOUSE_BUTTON_RIGHT) {
                if (listening) {
                    set();
                } else {
                    listening = true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    private void set() {
        setting.setValue(str.toString());

        str = new StringBuilder();
        ctrlz.add(str);
        count = ctrlz.size() - 1;
        listening = false;
    }
    public static boolean isAllowedCharacter(char character) {
        return character != 167 && character >= ' ' && character != 127;
    }
    public String getIdleSign() {
        if (idleTimer.passed(500L)) {
            idling = !idling;
            idleTimer.reset();
        }
        if (idling) {
            return "|";
        }
        return "";
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
    public boolean keyTyped(int keyCode, int scanCode, int modifiers) {
        if (isCopy(keyCode) && (listening || isHovering(mouseX, mouseY))) {
            mc.keyboardHandler.setClipboard(listening ? str.toString() : (String) setting.getValue());
            return true;
        }
        if (isPaste(keyCode) && (listening || isHovering(mouseX, mouseY))) {
            str = new StringBuilder(mc.keyboardHandler.getClipboard());
            return true;
        }
        if (isCTRLZ(keyCode) && (listening || isHovering(mouseX, mouseY))) {
            str = ctrlz.get(count = Math.min(count - 1, ctrlz.size() - 1));
            set();
            return true;
        }
        if (listening)
            switch (keyCode) {
                case GLFW_KEY_KP_ENTER, GLFW_KEY_ENTER -> {
                    set();
                }
                case GLFW_KEY_UP, GLFW_KEY_PAGE_UP -> str = ctrlz.get(count = Math.min(count + 1, ctrlz.size() - 1));
                case GLFW_KEY_DOWN, GLFW_KEY_PAGE_DOWN -> str = ctrlz.get(count = Math.max(count - 1, 0));
                case GLFW_KEY_BACKSPACE -> {
                    str.setLength(Math.max(str.length() - 1, 0));
                }
                case GLFW_KEY_DELETE -> str.setLength(0);
            }
        return super.keyTyped(keyCode, scanCode, modifiers);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char character) {
        if (listening && isAllowedCharacter(character)) str.append(character);
        return super.charTyped(character);
    }
}
