package stellarapi.feature.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import stellarapi.api.gui.overlay.IRawOverlaySet;
import stellarapi.api.gui.overlay.OverlayRegistry;

public class OverlayHandler {
	
	private OverlayContainer container;
	private OverlayManager manager;
	private Minecraft mc;

	public OverlayHandler() {
		this.container = new OverlayContainer();
		this.manager = new OverlayManager(this.container);
	}

	public void initialize(Minecraft mc) {
		this.mc = mc;
		OverlayRegistry.setupOverlay(this.manager);
		manager.initialize(mc);
		
		for(IRawOverlaySet set : manager.getDisplayedSets())
			if(set.getType() instanceof OverlaySetMain)
				set.setDisplayed();
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
