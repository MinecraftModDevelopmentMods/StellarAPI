package stellarapi.api.gui.overlay;

import stellarapi.api.gui.pos.EnumHorizontalPos;
import stellarapi.api.gui.pos.EnumVerticalPos;

/**
 * Certain overlay type.
 * Note that the Right Center part is already reserved by the position handling overlay.
 * */
public interface IOverlayType<Element extends IOverlayElement<Settings>, Settings extends PerOverlaySettings> {
	
	/** Generate the Overlay element. */
	public Element generateElement();
	
	/** Generate the Overlay settings. */
	public Settings generateSettings();
	
	/** Gets the name of certain overlay element, which will be the category name in the configuration. */
	public String getName();
	
	/** Gets the type name of the overlay. Will be used to distinguish overlays. */
	public String overlayType();
	
	/** Default Horizontal Position. */
	public EnumHorizontalPos defaultHorizontalPos();
	/** Default Vertical Position. */
	public EnumVerticalPos defaultVerticalPos();

	/** Whether this overlay type accepts certain position. */
	public boolean accepts(EnumHorizontalPos horizontal, EnumVerticalPos vertical);
	
	/** Generate Raw Handler. Can be null. */
	public IRawHandler<Element> generateRawHandler();

	/** Gives true iff. the overlay should be added on all of the overlay sets. */
	public boolean isUniversal();

}
