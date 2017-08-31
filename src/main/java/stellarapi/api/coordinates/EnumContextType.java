package stellarapi.api.coordinates;

public enum EnumContextType {
	/**
	 * For world-specific ones.
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