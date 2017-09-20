package stellarapi.api.coordinates;

public enum EnumContextType {
	/**
	 * For world-specific ones.
	 * Usually always contained in the context.
	 * */
	WORLD,

	/**
	 * For time-dependent ones.
	 * */
	TIME,

	/**
	 * For position-dependent ones.
	 *  (This actually needs patches)
	 * */
	POSITION;
}