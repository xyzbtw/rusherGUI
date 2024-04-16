package org.example.theme;

import org.example.theme.item.items.ModuleItem;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.ui.ElementBase;
import org.rusherhack.client.api.ui.panel.PanelHandlerBase;
import org.rusherhack.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.theme.Panel.run;

public class ClickGUIHandler extends PanelHandlerBase<Panel> {
    public ClickGUIHandler(boolean scaledWithMinecraftGui) {
        super(scaledWithMinecraftGui);
    }
    private double x1;

    @Override
    public void initialize() {
        x1 = 5;

        Arrays.stream(ModuleCategory.values()).forEach(moduleCategory -> {
            Panel panel = new Panel(this, moduleCategory.getName(), x1, 17);
            List<ModuleItem> items = new ArrayList<>();
            for(IModule module : RusherHackAPI.getModuleManager().getFeatures()) {
                if(module.getCategory() == moduleCategory) {
                    items.add(new ModuleItem(module, panel));
                }

            }

            panel.setModuleItems(items);
            addPanel(panel);
            x1 += panel.getWidth() + 6;
        });

        List<ModuleItem> pluginModules = new ArrayList<>();
        final ClassLoader rusherhackClassLoader = RusherHackAPI.getModuleManager().getFeature("Aura").get().getClass().getClassLoader();

        Panel pluginPanel = new Panel(this, "Plugins", x1, 17);

        for(IModule module : RusherHackAPI.getModuleManager().getFeatures()) {
            if(!module.getClass().getClassLoader().equals(rusherhackClassLoader)) {
                pluginModules.add(new ModuleItem(module, pluginPanel));
            }
        }

        if(!pluginModules.isEmpty()) {
            pluginPanel.setModuleItems(pluginModules);
            addPanel(pluginPanel);
        }


    }

    @Override
    public void setDefaultPositions() {

    }

    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        super.render(context, mouseX, mouseY);
        if(run != null) {
            run.run();
            run = null;
        }
    }
}
