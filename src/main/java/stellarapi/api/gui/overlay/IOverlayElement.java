package stellarapi.api.gui.overlay;

import net.minecraft.client.Minecraft;

/**
 * The main gui overlay.
 */
public interface IOverlayElement<Settings extends PerOverlaySettings> {

	public void initialize(Minecraft mc, Settings settings);

	public int getWidth();

	public int getHeight();

	public float animationOffsetX(float partialTicks);

	public float animationOffsetY(float partialTicks);

	public void switchMode(EnumOverlayMode mode);

	public void updateOverlay();

	/** Return true to update settings */
	public boolean mouseClicked(int mouseX, int mouseY, int eventButton);

	/** Return true to update settings */
	public boolean mouseClickMove(int scaledMouseX, int scaledMouseY, int eventButton, long timeSinceLastClick);

	/** Return true to update settings */
	public boolean mouseReleased(int mouseX, int mouseY, int eventButton);

	/** Return true to update settings */
	public boolean keyTyped(char eventChar, int eventKey);

	public void render(int mouseX, int mouseY, float partialTicks);

}
