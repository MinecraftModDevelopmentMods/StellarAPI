package stellarapi.api.gui.overlay;

import com.google.common.collect.ImmutableList;

public interface IOverlayManager {

	/** Switches the overlay mode. */
	public void switchMode(EnumOverlayMode enumOverlayMode);

	/** Gets the list of displayed overlay sets. */
	public ImmutableList<IRawOverlaySet> getDisplayedSets();

	/** Gets current element on certain mouse position. */
	public IRawOverlayElement getCurrentElement(int mouseX, int mouseY);

	/** Gets current displayed set. */
	public IRawOverlaySet getCurrentDisplayedSet();

	/** Gets current width of the window */
	public int getCurrentWidth();

	/** Gets current width of the window */
	public int getCurrentHeight();

	/** Check if the game is paused by one of the overlays. */
	public boolean isGamePaused();

	/** Sets game as paused, only effective when overlay screen is opened. */
	public void setGamePaused(boolean pause);

}
