package stellarapi.feature.gui.overlay.configurator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import stellarapi.api.gui.overlay.EnumOverlayMode;
import stellarapi.api.gui.overlay.IOverlayElement;
import stellarapi.api.gui.overlay.IRawOverlaySet;
import stellarapi.api.gui.overlay.PerOverlaySettings;
import stellarapi.lib.gui.button.GuiButtonColorable;

public class OverlayConfigurator implements IOverlayElement<PerOverlaySettings> {
	private static final int WIDTH = 60;
	private static final int BTN_HEIGHT = 20;
	private static final int HEIGHT = BTN_HEIGHT * 3;
	private static final int ANIMATION_DURATION = 10;

	private Minecraft mc;
	EnumOverlayMode currentMode = EnumOverlayMode.OVERLAY;

	IRawOverlaySet currentSet = null;
	boolean gamePaused = false;

	private GuiButtonColorable button, btnOverlaySet, btnGamePaused;
	private int animationTick = 0;

	boolean markForUpdate = false;
	boolean markForUpdateSet = false;
	boolean markForUpdatePause = false;

	@Override
	public void initialize(Minecraft mc, PerOverlaySettings settings) {
		this.mc = mc;

		this.button = new GuiButtonColorable(0, 0, 0, WIDTH, BTN_HEIGHT, I18n.format(
				currentMode == EnumOverlayMode.POSITION ? "gui.configurator.stop" : "gui.configurator.position"));
		this.btnOverlaySet = new GuiButtonColorable(0, 0, BTN_HEIGHT, WIDTH, BTN_HEIGHT, "");
		this.btnGamePaused = new GuiButtonColorable(0, 0, BTN_HEIGHT * 2, WIDTH, BTN_HEIGHT, "");
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	public float animationOffsetX(float partialTicks) {
		return 0.0f;
	}

	@Override
	public float animationOffsetY(float partialTicks) {
		return 0.0f;
	}

	@Override
	public void switchMode(EnumOverlayMode mode) {
		if (currentMode.displayed() != mode.displayed()) {
			if (mode.displayed())
				animationTick = 0;
			else
				animationTick = ANIMATION_DURATION;
		}
		this.currentMode = mode;

		button.displayString = I18n.format(
				currentMode == EnumOverlayMode.POSITION ? "gui.configurator.stop" : "gui.configurator.position");
	}

	@Override
	public void updateOverlay() {
		if (this.animationTick > 0 && !currentMode.displayed())
			this.animationTick--;
		else if (this.animationTick < ANIMATION_DURATION && currentMode.displayed())
			this.animationTick++;
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int eventButton) {
		if (currentMode.displayed()) {
			if (button.mousePressed(this.mc, mouseX, mouseY))
				this.markForUpdate = true;
			if (btnOverlaySet.mousePressed(this.mc, mouseX, mouseY))
				this.markForUpdateSet = true;
			if (btnGamePaused.mousePressed(this.mc, mouseX, mouseY))
				this.markForUpdatePause = true;
		}

		return false;
	}

	@Override
	public boolean mouseReleased(int mouseX, int mouseY, int eventButton) {
		if (currentMode.displayed()) {
			button.mouseReleased(mouseX, mouseY);
			btnOverlaySet.mouseReleased(mouseX, mouseY);
			btnGamePaused.mouseReleased(mouseX, mouseY);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char eventChar, int eventKey) {
		return false;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		partialTicks = currentMode.displayed() ? partialTicks : -partialTicks;
		btnOverlaySet.alpha = button.alpha = btnGamePaused.alpha = (this.animationTick + partialTicks)
				/ ANIMATION_DURATION;

		if (this.currentSet != null)
			btnOverlaySet.displayString = I18n.format(currentSet.getType().getLanguageKey());
		btnGamePaused.displayString = I18n
				.format(this.gamePaused ? "gui.configurator.paused" : "gui.configurator.playing");

		button.drawButton(this.mc, mouseX, mouseY);
		btnOverlaySet.drawButton(this.mc, mouseX, mouseY);
		btnGamePaused.drawButton(this.mc, mouseX, mouseY);
	}

	@Override
	public boolean mouseClickMove(int scaledMouseX, int scaledMouseY, int eventButton, long timeSinceLastClick) {
		return false;
	}

}
