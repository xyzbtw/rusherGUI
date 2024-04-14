package org.example.theme;

import org.example.theme.item.items.ModuleItem;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.ui.ElementBase;
import org.rusherhack.client.api.ui.panel.PanelHandlerBase;

import java.util.*;

public class ClickGUIHandler extends PanelHandlerBase<ElementBase> {
    public ClickGUIHandler(boolean scaledWithMinecraftGui) {
        super(scaledWithMinecraftGui);
    }
    private double x1;

    @Override
    public void initialize() {
        x1 = 5;

        Arrays.stream(ModuleCategory.values()).forEach(moduleCategory -> {
            Panel panel = new Panel(this, moduleCategory, x1, 17);
            List<ModuleItem> items = new ArrayList<>();

            for(IModule module : RusherHackAPI.getModuleManager().getFeatures()) {
                if(module.getCategory() == moduleCategory) {
                    items.add(new ModuleItem(module, panel));
                }
            }

            panel.setModuleItems(items);
            addPanel(panel);
            System.out.println("Added new panel " + panel.getName());
            x1 += panel.getWidth() +6;
        });


    }

    @Override
    public void setDefaultPositions() {

    }


}
