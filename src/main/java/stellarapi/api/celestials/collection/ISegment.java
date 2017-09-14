package stellarapi.api.celestials.collection;

public interface ISegment {
	/**
	 * Gets property for certain key.
	 * Throws ClassCastException when the type does not match.
	 * */
	public <T> T getProperty(String key);
}
