package stellarapi.feature.gui.overlay;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import stellarapi.api.gui.overlay.EnumOverlayMode;

public class GuiScreenOverlay extends GuiScreen {

	private OverlayContainer container;
	private KeyBinding focusGuiKey;

	public GuiScreenOverlay(OverlayContainer container, KeyBinding focusGuiKey) {
		this.container = container;
		this.focusGuiKey = focusGuiKey;

		container.switchMode(EnumOverlayMode.FOCUS);
	}

	@Override
	public void onGuiClosed() {
		container.switchMode(EnumOverlayMode.OVERLAY);
	}

	@Override
	public void initGui() {
		ScaledResolution resolution = new ScaledResolution(this.mc);
		container.setResolution(resolution);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int eventButton) {
		container.mouseClicked(mouseX, mouseY, eventButton);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int eventButton) {
		container.mouseReleased(mouseX, mouseY, eventButton);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		container.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {
		if (eventKey == Keyboard.KEY_ESCAPE || eventKey == focusGuiKey.getKeyCode()) {
			this.mc.displayGuiScreen((GuiScreen) null);
			this.mc.setIngameFocus();
		}

		container.keyTyped(eventChar, eventKey);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return container.isGamePaused();
	}

}
