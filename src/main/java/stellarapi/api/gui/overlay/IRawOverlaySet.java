package stellarapi.api.gui.overlay;

import com.google.common.collect.ImmutableList;

public interface IRawOverlaySet {
	
	public IOverlaySetType getType();
	
	public void setDisplayed();
	
	public boolean doesContain(IRawOverlayElement element);
	public ImmutableList<IRawOverlayElement> getContainedElements();

	public boolean addElement(IRawOverlayElement element);
	public void removeElement(IRawOverlayElement element);

}
