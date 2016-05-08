package stellarapi.api.gui.overlay;

import net.minecraft.client.Minecraft;
import stellarapi.feature.gui.overlay.OverlayManager;

/**
 * Raw handler to control the overlay with the container.
 * */
public interface IRawHandler<Element extends IOverlay> {
	
	/**
	 * Initialize the raw handler.
	 * */
	public void initialize(Minecraft mc, IOverlayManager manager, Element element);
	
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
