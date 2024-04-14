package org.example.theme.item.items;

import lombok.Getter;
import lombok.Setter;
import org.example.theme.Panel;
import org.lwjgl.glfw.GLFW;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.setting.BindSetting;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.client.api.ui.ElementBase;
import org.rusherhack.client.api.ui.panel.IPanelItem;
import org.rusherhack.core.setting.*;

import java.util.ArrayList;
import java.util.List;

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
