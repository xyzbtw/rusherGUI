package dev.sf.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sf.ui.guistuff.MainTheme;
import dev.sf.ui.utils.shaders.Shaders;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

/**
 * Example rusherhack plugin
 *
 * @author John200410
 */
public class ExamplePlugin extends Plugin {
	public static MainTheme theme = new MainTheme();

	@Override
	public void onLoad() {
		RusherHackAPI.getThemeManager().registerTheme(theme);
		RenderSystem.recordRenderCall(() -> Shaders.init());
	}
	
	@Override
	public void onUnload() {
		this.getLogger().info("Example plugin unloaded!");
	}
	
}