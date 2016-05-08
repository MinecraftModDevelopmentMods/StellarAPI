package stellarapi.api.gui.overlay;

import stellarapi.api.gui.pos.ElementPos;
import stellarapi.api.gui.pos.EnumHorizontalPos;
import stellarapi.api.gui.pos.EnumVerticalPos;

public interface IRawOverlayElement {

	public IOverlayType getType();

	public EnumHorizontalPos getCurrentHorizontalPos();
	public EnumVerticalPos getCurrentVerticalPos();

	public boolean acceptPos(EnumHorizontalPos horizontal, EnumVerticalPos vertical);
	public void setPos(EnumHorizontalPos horizontal, EnumVerticalPos vertical);

	public String getId();

	/** Gets the mod id. This will be used to distinguish certain overlay type. */
	public String getModId();

	public int getWidth();
	public int getHeight();

}
