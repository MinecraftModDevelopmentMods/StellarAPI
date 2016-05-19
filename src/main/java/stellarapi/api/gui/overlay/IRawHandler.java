package stellarapi.api.gui.overlay;

import net.minecraft.client.Minecraft;

/**
 * Raw handler to control the overlay with the container.
 * */
public interface IRawHandler<Element extends IOverlayElement> {
	
	/**
	 * Initialize the raw handler.
	 * */
	public void initialize(Minecraft mc, IOverlayManager manager, Element element);
	
	/** Update on every tick. */
	public void updateHandler();
	
	/**
	 * Return true to update settings.
	 * Checked after element.
	 * */
	public boolean mouseClicked(int mouseX, int mouseY, int eventButton);
	
	/**
	 * Return true to update settings.
	 * Checked after element.
	 * */
	public boolean mouseReleased(int mouseX, int mouseY, int eventButton);
	
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
