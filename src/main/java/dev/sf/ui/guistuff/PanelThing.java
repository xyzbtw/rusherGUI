package dev.sf.ui.guistuff;

import org.rusherhack.client.api.render.IRenderer2D;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.ui.panel.PanelHandlerBase;

public class PanelThing extends PanelHandlerBase<ElementThing> {
    public PanelThing(boolean scaledWithMinecraftGui) {
        super(scaledWithMinecraftGui);
    }

    @Override
    public void initialize() {
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
