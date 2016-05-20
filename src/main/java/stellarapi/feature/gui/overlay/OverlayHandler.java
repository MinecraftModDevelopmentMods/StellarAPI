package stellarapi.feature.gui.overlay;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
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

	public void renderGameOverlay(ScaledResolution resolution, float partialTicks) {
		int mouseX = -100, mouseY = -100;
		if(mc.currentScreen != null) {
			mouseX = Mouse.getEventX() * resolution.getScaledWidth() / mc.displayWidth;
			mouseY = resolution.getScaledHeight() - Mouse.getEventY() * resolution.getScaledHeight() / mc.displayHeight - 1;
		}
		
		container.setResolution(resolution);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		container.render(mouseX, mouseY, partialTicks);
		GlStateManager.disableBlend();
	}
}
