package stellarapi.api.coordinates;

public class PositionDependent {
	/**
	 * Flag which represents whether coordinates depends on position on world or not.
	 * Should be enabled with reflection.
	 * */
	private static final boolean positionDependent = false;

	/** Public getter for the flag */
	public static boolean isPositionDependent() {
		return positionDependent;
	}
}
