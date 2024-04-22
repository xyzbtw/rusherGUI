package org.example.theme.item.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Tuple;
import org.example.theme.ExamplePlugin;
import org.example.theme.Panel;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.render.IRenderer2D;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.render.font.IFontRenderer;
import org.rusherhack.client.api.setting.BindSetting;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.client.api.ui.ElementBase;
import org.rusherhack.client.api.ui.panel.IPanelItem;
import org.rusherhack.core.setting.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.rusherhack.client.api.Globals.mc;

public class ExtendableItem extends ElementBase implements IPanelItem {
    ExtendableItem parent;
    IModule module;
    Panel panel;
    Setting setting;
    public double mouseX = 0, mouseY = 0, rendererHeight = 11F;
    public boolean open;
    public  IRenderer2D renderer = RusherHackAPI.getRenderer2D();
    public IFontRenderer fontRenderer = RusherHackAPI.fonts().getFontRenderer();
    public List<ExtendableItem> subItems = new ArrayList<>();
    public ExtendableItem(ExtendableItem parent, IModule module, Panel panel, Setting<?> settingValue) {
        this.parent = parent;
        this.module = module;
        this.panel = panel;
        this.setting = settingValue;
        if(setting!=null && !setting.getSubSettings().isEmpty())
            addSettingItems(setting.getSubSettings());
    }
    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        possibleHeightUpdate();
        this.mouseY = mouseY;
        this.mouseX = mouseX;
    }
    public void renderSubItems(RenderContext context, double mouseX, double mouseY, List<ExtendableItem> subItems, Boolean open) {
        if(!subItems.isEmpty() && open) {
            renderer.getMatrixStack().pushPose();
            double height = 12f;
            for (ExtendableItem subItem : subItems) {
                if(subItem.setting.isHidden()) continue;

                subItem.setX(getX() + 3);
                subItem.setY(getY() + height);

                subItem.render(context, mouseX, mouseY);
                height += subItem.getHeight(true) + 0.5F;
            }
            renderer.getMatrixStack().popPose();
        }
    }

    @Override
    public double getWidth() {
        return parent.getWidth() - 3;
    }

    @Override
    public double getHeight(boolean total) {
        if(total){
            return rendererHeight;
        }
        return 11;
    }
    public void drawDesc(IRenderer2D mesh2D, double x, double y, String text) {
        Runnable runnable = () -> {
            mesh2D.getMatrixStack().pushPose();
            mesh2D.getMatrixStack().translate(0F, 0F, 200F);
            IFontRenderer fontRenderer = RusherHackAPI.fonts().getFontRenderer();
            List<Tuple<Float, String>> pairs = new ArrayList<>();
            String[] lines = text.split("\n");
            float offset = 0;
            for (String s : lines) {
                pairs.add(new Tuple<>(offset, s));
                offset += (float) fontRenderer.getFontHeight() + 0.3f;
            }
            double maxWidth = Arrays.stream(lines)
                    .map(fontRenderer::getStringWidth)
                    .max(Comparator.comparing(i -> i)).orElse(0.0);
            double diff = Math.max(0, x + maxWidth - mc.getWindow().getGuiScaledWidth());
            double x0 = x - (diff + (diff > 0 ? 1F : 0F));
            mesh2D.drawRectangle(x0 - 0.5F, y - 0.5F, maxWidth + 0.5F, offset, new Color(0, 0, 0, 200).getRGB());

            for (Tuple<Float, String> pair : pairs) {
                fontRenderer.drawString(pair.getB(), x0, y + pair.getA() - 1F, ExamplePlugin.theme.fontColor.getValue().getRGB());
            }
            mesh2D.getMatrixStack().popPose();
        };
        Panel.setRun(runnable);
    }
    @Override
    public double getX(){
        return panel.getX() + 1.5;
    }

    @Override
    public boolean isHovered(double mouseX, double mouseY, boolean includeSubItems) {
        return isHovering(mouseX, mouseY);
    }

    @Override
    public double getHeight() {
        return getHeight(false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        subItems.forEach(subItem -> {
            subItem.mouseClicked(mouseX, mouseY, button);
        });
        return false;
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        IPanelItem.super.mouseReleased(mouseX, mouseY, button);
        subItems.forEach(subItem -> {
            subItem.mouseReleased(mouseX, mouseY, button);
        });
    }

    @Override
    public boolean charTyped(char character) {
        subItems.forEach(subItem -> {
            subItem.charTyped(character);
        });
        return false;
    }

    @Override
    public boolean keyTyped(int key, int scanCode, int modifiers) {
        subItems.forEach(subItem -> {
            subItem.keyTyped(key, scanCode, modifiers);
        });

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
    public void addSubItem(ExtendableItem item) {
        this.subItems.add(item);
    }
    public void addSettingItems(List<Setting<?>> settings) {
        for(Setting<?> setting : settings) {
            if(setting instanceof BooleanSetting) {
                this.addSubItem(new BooleanItem(this.module, this.panel, this, (BooleanSetting) setting));
            }
            else if(setting instanceof StringSetting) {
                //string settings can either act like enum settings, or be configurable strings
                if (!((StringSetting) setting).getOptions().isEmpty()) {
                    this.addSubItem(new EnumItem(this, module, panel, setting));
                } else {
                    this.addSubItem(new StringItem(this, module, panel, setting));
                }
            }
            else if(setting instanceof EnumSetting<?>){
                this.addSubItem(new EnumItem(this, module, panel, setting));
            }
            else if(setting instanceof BindSetting){
                this.addSubItem(new BindItem(this, module, panel, setting, false));
            }
            else if(setting instanceof NumberSetting<?>){
                this.addSubItem(new NumberItem(this, module, panel, setting));
            }
            else if(setting instanceof NullSetting){
                this.addSubItem(new NullItem(this, module, panel, setting));
            }
            else if(setting instanceof ColorSetting){
                this.addSubItem(new ColorItem(this, module, panel, setting));
            }
        }
    }
    protected void possibleHeightUpdate() {
        double temp = 11.5f;
        if (open)
            temp += subItems.stream().mapToDouble(i -> i.setting.isHidden() ? 0 : (i.getHeight(true) + 0.5f)).sum();
        rendererHeight = temp;
    }

}
