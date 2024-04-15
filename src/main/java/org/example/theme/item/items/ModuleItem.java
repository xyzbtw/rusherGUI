package org.example.theme.item.items;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Tuple;
import org.example.theme.ExamplePlugin;
import org.example.theme.Panel;
import org.lwjgl.glfw.GLFW;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.render.IRenderer2D;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.render.font.IFontRenderer;
import org.rusherhack.client.api.setting.BindSetting;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.client.api.ui.ElementBase;
import org.rusherhack.client.api.ui.ElementHandlerBase;
import org.rusherhack.client.api.ui.panel.IPanelItem;
import org.rusherhack.core.setting.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.rusherhack.client.api.Globals.mc;

public class ModuleItem extends ElementBase implements IPanelItem {
    IModule module;
    Panel panel;
    private boolean open = false;
    private final ArrayList<IPanelItem> subItems = new ArrayList<>();
    private double prevHeight, rendererHeight;

    public ModuleItem(IModule module, Panel panel){
        this.module = module;
        this.panel = panel;

        addSettingItems(module.getSettings());

        rendererHeight = 11F;
        prevHeight = (float) (subItems.stream()
                .filter(IPanelItem::isVisible)
                .mapToDouble(frame -> frame.getHeight(true) + .5)
                .sum() + getHeight(false));
    }
    @Override
    public double getWidth() {
        return panel.getWidth() - 2;
    }

    @Override
    public double getHeight() {
        return 11;
    }

    @Override
    public double getHeight(boolean total) {
        return 11;
    }

    @Override
    public boolean isHovered(double mouseX, double mouseY, boolean includeSubItems) {
        return false;
    }

    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        final IRenderer2D renderer = RusherHackAPI.getRenderer2D();
        final IFontRenderer fontRenderer = RusherHackAPI.fonts().getFontRenderer();
        possibleHeightUpdate();

        if(module instanceof ToggleableModule){
            renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(true),
                    ((ToggleableModule) module).isToggled()
                        ? new Color(155, 255, 0, 50).getRGB()
                        : ExamplePlugin.theme.colorSetting.getValue().getRGB());
        }else renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(true), new Color(155, 255, 0, 50).getRGB());
        fontRenderer.drawString(module.getName(), getX() + 3.5f, (panel.isHovering(mouseX, mouseY, getX(), getY(), getWidth(), getHeight()) ? 1F + getY() : 2F + getY()) - 1F, ExamplePlugin.theme.fontColor.getValue().getRGB());


        if(panel.isHovering(mouseX,mouseY, getX(), getY(), getWidth(), getHeight())) {
            renderer.drawRectangle(getX(), getY(), getWidth(), getHeight(true), new Color(0, 0, 0, 50).getRGB());
            String description =
                    (module.getDescription().isEmpty() ?
                            "A " + module.getCategory() +" Module." + ChatFormatting.GREEN + " Name" + ChatFormatting.RESET + " «" +  module.getName() + "»."
                            : module.getDescription());

            drawDesc(renderer, mouseX + 8,mouseY + 8, description);
        }
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
                offset += (float) mesh2D.getFontRenderer().getFontHeight();
            }
            double maxWidth = Arrays.stream(lines)
                    .map(fontRenderer::getStringWidth)
                    .max(Comparator.comparing(i -> i)).orElse(0.0);
            double diff = Math.max(0, x + maxWidth - mc.getWindow().getGuiScaledWidth());
            double x0 = x - (diff + (diff > 0 ? 1F : 0F));
            mesh2D.drawOutlinedRectangle(x0 - 0.5F, y - 0.5F, maxWidth + 0.5F, offset, 1, new Color(0, 0, 0, 50).getRGB(), new Color(0, 0, 0, 50).getRGB());

            for (Tuple<Float, String> pair : pairs) {
                fontRenderer.drawString(pair.getB(), x0, y + pair.getA() - 1F, ExamplePlugin.theme.fontColor.getValue().getRGB());
            }
        };
        runnable.run();

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_1 && panel.isHovering(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
            if(module instanceof ToggleableModule){
                ((ToggleableModule) module).toggle();
            }
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_2 && panel.isHovering(mouseX, mouseY, getX(), getY(), getWidth(), getHeight())) {
            this.open = !this.open;
            possibleHeightUpdate();
        }
        return false;
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        IPanelItem.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char character) {
        return false;
    }

    @Override
    public boolean keyTyped(int key, int scanCode, int modifiers) {
        return false;
    }
    protected void possibleHeightUpdate() {
        double temp;
        if (open)
            temp = (float) (subItems.stream()
                    .filter(IPanelItem::isVisible)
                    .mapToDouble(frame -> frame.getHeight(true) + .5)
                    .sum() + getHeight(true));
        else {
            temp = getHeight(false);
        }
        if (this.rendererHeight == temp){
            return;
        }
        prevHeight = this.rendererHeight;
        this.rendererHeight = temp;
    }
    public void addSettingItems(List<Setting<?>> settings) {
        for(Setting<?> setting : settings) {
//            if(setting instanceof BooleanSetting) {
//                this.addSubItem(new ClassicBooleanItem(this.getPanel(), this, (BooleanSetting) setting));
//            } else if(setting instanceof NumberSetting<?>) {
//                this.addSubItem(new ClassicSliderItem(this.getPanel(), this, (NumberSetting<?>) setting));
//            } else if(setting instanceof EnumSetting<?>) {
//                this.addSubItem(new ClassicOptionsItem(this.getPanel(), this, (EnumSetting<?>) setting));
//            } else if(setting instanceof StringSetting) {
//                //string settings can either act like enum settings, or be configurable strings
//                if(!((StringSetting) setting).getOptions().isEmpty()) {
//                    this.addSubItem(new ClassicOptionsItem(this.getPanel(), this, (StringSetting) setting));
//                } else {
//                    this.addSubItem(new ClassicStringItem(this.getPanel(), this, (StringSetting) setting));
//                }
//            } else if(setting instanceof ColorSetting) {
//                this.addSubItem(new ClassicColorItem(this.getPanel(), this, (ColorSetting) setting));
//            } else if(setting instanceof BindSetting) {
//                this.addSubItem(new ClassicBindItem(this.getPanel(), this, (BindSetting) setting));
//            } else if(setting instanceof NullSetting) {
//                this.addSubItem(new ClassicSettingItem<>(this.getPanel(), this, (NullSetting) setting));
//            }
        }
    }
    public void addSubItem(IPanelItem item) {
        this.subItems.add(item);
    }
}
