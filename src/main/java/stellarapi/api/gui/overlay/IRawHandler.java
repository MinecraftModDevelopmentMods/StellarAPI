package stellarapi.api.gui.overlay;

import net.minecraft.client.Minecraft;

/**
 * Raw handler to control the overlay.
 * */
public interface IRawHandler<Element extends IGuiOverlay> {
	
	/**
	 * Initialize the raw handler.
	 * */
	public void initialize(Minecraft mc, OverlayContainer container, Element element);
	
	/**
	 * Return true to update settings.
	 * Checked after element.
	 * */
	public boolean mouseClicked(int mouseX, int mouseY, int eventButton);
	
	/**
	 * Return true to update settings.
	 * Checked after element.
	 * */
	public boolean mouseMovedOrUp(int mouseX, int mouseY, int eventButton);
	
	/**
	 * Return true to update settings.
	 * Checked after element.
	 * */
	public boolean keyTyped(char eventChar, int eventKey);

	/**
	 * Renders this raw handler.
	 * */
	public void render(int mouseX, int mouseY, float partialTicks);

}
