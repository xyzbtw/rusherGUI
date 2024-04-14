package org.example.guistuff;

import net.minecraft.client.gui.Font;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.render.IRenderer2D;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.render.font.IFontRenderer;
import org.rusherhack.client.api.ui.panel.PanelHandlerBase;

import java.awt.*;

public class PanelThing extends PanelHandlerBase<ElementThing> {
    public PanelThing(boolean scaledWithMinecraftGui) {
        super(scaledWithMinecraftGui);
    }

    @Override
    public void initialize() {
        System.out.println("initialized ASDYDYASSDDSA");
        addPanel(new PanelItemThing());
    }

    @Override
    public void setDefaultPositions() {
        System.out.println("set default positions");
    }

    @Override
    public void render(RenderContext context) {

    }

    @Override
    public IRenderer2D getRenderer() {
        return super.getRenderer();
    }
}
