package stellarapi.feature.gui.overlay;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import stellarapi.api.gui.overlay.OverlayContainer;

public class OverlayHandler {
	
	private Map<String, OverlayContainer> container = Maps.newHashMap();
	private Minecraft mc;
	private String activeContainer;
	
	public OverlayHandler() {
		;
	}
	
	public void initialize(Minecraft mc) {
		this.mc = mc;
		container.initialize(mc);
	}
	
	public void updateOverlay() {
		container.updateOverlay();
	}
	
	public void openGui(Minecraft mc, KeyBinding focusGuiKey) {
		mc.displayGuiScreen(new GuiScreenOverlay(this.container, focusGuiKey));
	}

	public void renderGameOverlay(ScaledResolution resolution, int mouseX, int mouseY, float partialTicks) {
		container.setResolution(resolution);
		container.render(mouseX, mouseY, partialTicks);
	}
}
