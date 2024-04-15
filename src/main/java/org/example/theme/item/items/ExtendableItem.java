package org.example.theme.item.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Tuple;
import org.example.theme.ExamplePlugin;
import org.example.theme.Panel;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.command.arg.SettingValue;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.render.IRenderer2D;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.render.font.IFontRenderer;
import org.rusherhack.client.api.ui.ElementBase;
import org.rusherhack.client.api.ui.panel.IPanelItem;
import org.rusherhack.core.setting.Setting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.rusherhack.client.api.Globals.mc;

public class ExtendableItem extends ElementBase implements IPanelItem {
    ModuleItem parent;
    IModule module;
    Panel panel;
    Setting setting;
    public double mouseX = 0, mouseY = 0;
    public  IRenderer2D renderer = RusherHackAPI.getRenderer2D();
    public IFontRenderer fontRenderer = RusherHackAPI.fonts().getFontRenderer();
    public ExtendableItem(ModuleItem parent, IModule module, Panel panel, Setting<?> settingValue) {
        this.parent = parent;
        this.module = module;
        this.panel = panel;
        this.setting = settingValue;
    }
    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        this.mouseY = mouseY;
        this.mouseX = mouseX;
    }

    @Override
    public double getWidth() {
        return parent.getWidth() - 3;
    }

    @Override
    public double getHeight(boolean total) {
        return 11;
    }
    public void drawDesc(IRenderer2D mesh2D, double x, double y, String text) {
        mesh2D.getMatrixStack().pushPose();
        mesh2D.getMatrixStack().translate(0F, 0F, 200F);
        IFontRenderer fontRenderer = RusherHackAPI.fonts().getFontRenderer();
        List<Tuple<Float, String>> pairs = new ArrayList<>();
        String[] lines = text.split("\n");
        float offset = 0;
        for (String s : lines) {
            pairs.add(new Tuple<>(offset, s));
            offset += (float) fontRenderer.getFontHeight();
        }
        double maxWidth = Arrays.stream(lines)
                .map(fontRenderer::getStringWidth)
                .max(Comparator.comparing(i -> i)).orElse(0.0);
        double diff = Math.max(0, x + maxWidth - mc.getWindow().getGuiScaledWidth());
        double x0 = x - (diff + (diff > 0 ? 1F : 0F));
        mesh2D.drawRectangle(x0 - 0.5F, y - 0.5F, maxWidth + 0.5F, offset, new Color(0, 0, 0, 50).getRGB());

        for (Tuple<Float, String> pair : pairs) {
            fontRenderer.drawString(pair.getB(), x0, y + pair.getA() - 1F, ExamplePlugin.theme.fontColor.getValue().getRGB());
        }
        mesh2D.getMatrixStack().popPose();
    }
    @Override
    public double getX(){
        return super.getX() + 1.5;
    }

    @Override
    public boolean isHovered(double mouseX, double mouseY, boolean includeSubItems) {
        return false;
    }

    @Override
    public double getHeight() {
        return 11;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean charTyped(char character) {
        return false;
    }

    @Override
    public boolean keyTyped(int key, int scanCode, int modifiers) {
        return false;
    }
    protected boolean isHovering(double mouseX, double mouseY) {
        return mouseX >= getX()
                && mouseX <= getX() + getWidth()
                && mouseY >= getY()
                && mouseY <= getY() + getHeight()

                && mouseX >= panel.getX()
                && mouseX <= panel.getX() + panel.getWidth()
                && mouseY >= panel.getY()
                && mouseY <= panel.getY() + panel.getHeight();
    }
    public static void setClipboardString(String copyText) {

        try {
            mc.keyboardHandler.setClipboard(copyText);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static String getClipboardString() {
        try {
            return mc.keyboardHandler.getClipboard();
        } catch (Exception var2) {
            var2.printStackTrace();
        }
        return "";
    }
    public final boolean isCopy(int key) {
        return Screen.isCopy(key) || Screen.isCut(key);
    }
    public final boolean isPaste(int key) {
        return Screen.isPaste(key);
    }
    public final String wrapString(String type, String value) {
        return String.format("[%s]:%s", type, value);
    }
    public final String getValueFromWrappedString(String type, String wrapped) {
        if (wrapped == null || type == null) return null;
        if (!wrapped.startsWith("[" + type + "]:")) return null;
        return wrapped.replace("[" + type + "]:", "");

    }
    public final boolean isCTRLZ(int key) {
        return key == GLFW_KEY_Z && Screen.hasControlDown() && !Screen.hasShiftDown() && !Screen.hasAltDown();
    }
    public final boolean isCTRLR(int key) {
        return key == GLFW_KEY_R && Screen.hasControlDown() && !Screen.hasShiftDown() && !Screen.hasAltDown();
    }
    protected boolean isHovering(double mouseX, double mouseY, double x, double y, double width, double height) {
        return mouseX >= x
                && mouseX <= width
                && mouseY >= y
                && mouseY <= height

                && mouseX >= panel.getX()
                && mouseX <= panel.getX() + panel.getWidth()
                && mouseY >= panel.getY()
                && mouseY <= panel.getY() + panel.getHeight();
    }
}
