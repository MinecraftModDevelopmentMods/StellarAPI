package stellarapi.api.gui.overlay;

public enum EnumOverlayMode {
	OVERLAY(false, false),
	FOCUS(true, true),
	POSITION(false, true);
	
	private final boolean focused;
	private final boolean displayed;
	
	EnumOverlayMode(boolean focused, boolean displayed) {
		this.focused = focused;
		this.displayed = displayed;
	}
	
	/**
	 * Force focus or not
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
