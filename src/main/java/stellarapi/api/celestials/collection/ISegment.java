package stellarapi.api.celestials.collection;

import javax.annotation.Nullable;

public interface ISegment {
	/**
	 * Gets property for certain key.
	 * gives null if there's no property for certain key.
	 * Throws ClassCastException when the type does not match.
	 * */
	public @Nullable <T> T getProperty(String key);
}