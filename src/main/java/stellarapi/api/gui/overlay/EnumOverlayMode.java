package stellarapi.api.gui.overlay;

public enum EnumOverlayMode {
	/**
	 * Mode for normal overlay.
	 * In this mode, the gui is neither focused nor forced display.
	 * */
	OVERLAY(false, false),
	
	/**
	 * Mode for focused overlay.
	 * In this mode, the gui is both focused and forced display.
	 * */
	FOCUS(true, true),
	
	/**
	 * Mode for customizing positions and availability.
	 * In this mode, the gui is not focused but forced to be displayed.
	 * */
	POSITION(false, true);
	
	private final boolean focused;
	private final boolean displayed;
	
	EnumOverlayMode(boolean focused, boolean displayed) {
		this.focused = focused;
		this.displayed = displayed;
	}
	
	/**
	 * Focused or not
	 * */
	public boolean focused() {
		return this.focused;
	}
	
	/**
	 * Force display or not
	 * */
	public boolean displayed() {
		return this.displayed;
	}
}
