package org.example.theme.item.items;

import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.ui.ElementBase;
import org.rusherhack.client.api.ui.panel.IPanelItem;

public class ExtendableItem extends ElementBase implements IPanelItem {
    ModuleItem parent;
    public ExtendableItem(ModuleItem parent) {
        this.parent = parent;
    }
    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {

    }

    @Override
    public double getWidth() {
        return parent.getWidth() - 3;
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
    public double getHeight() {
        return 0;
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
}
