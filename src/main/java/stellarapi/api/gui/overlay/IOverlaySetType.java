package stellarapi.api.gui.overlay;

public interface IOverlaySetType {
	
	/** Gets ths language key for the set. */
	public String getLanguageKey();
	
	/**
	 * Accept certain overlay element by default, or not.
	 * @param overlay the raw overlay element
	 * */
	public boolean acceptOverlayByDefault(IRawOverlayElement overlay);

}
