package stellarapi.api.gui.overlay;

import stellarapi.api.gui.pos.EnumHorizontalPos;
import stellarapi.api.gui.pos.EnumVerticalPos;

/**
 * Certain overlay type.
 * Note that the Right Center part is already reserved by the position handling overlay.
 * */
public interface IGuiOverlayType<Element extends IGuiOverlay<Settings>, Settings extends PerOverlaySettings> {
	
	/** Generate the Overlay element. */
	public Element generateElement();
	
	/** Generate the Overlay settings. */
	public Settings generateSettings();
	
	/** Gets the name of certain overlay element. */
	public String getName();
	
	/** Default Horizontal Position. */
	public EnumHorizontalPos defaultHorizontalPos();
	/** Default Vertical Position. */
	public EnumVerticalPos defaultVerticalPos();

	/** Whether this overlay type accepts certain position. */
	public boolean accepts(EnumHorizontalPos horizontal, EnumVerticalPos vertical);
	
	/** Generate Raw Handler. Can be null. */
	public IRawHandler<Element> generateRawHandler();

}
