package stellarapi.api.gui;

import stellarapi.api.gui.pos.EnumHorizontalPos;
import stellarapi.api.gui.pos.EnumVerticalPos;

public interface IGuiOverlayType<Element extends IGuiOverlay<Settings>, Settings extends PerOverlaySettings> {
	
	public Element generateElement();
	public Settings generateSettings();
	public String getName();
	
	public EnumHorizontalPos defaultHorizontalPos();
	public EnumVerticalPos defaultVerticalPos();

	public boolean accepts(EnumHorizontalPos horizontal, EnumVerticalPos vertical);
	
	/**Can be null*/
	public IRawHandler<Element> generateRawHandler();

}
