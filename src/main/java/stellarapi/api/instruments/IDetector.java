package stellarapi.api.instruments;

/**
 * The detector, which is also an optical instrument by definition.
 * */
public interface IDetector {
	/**
	 * Checks if the display should be in accurate mode
	 *  on this detector with specified instrument.
	 * @throws IllegalArgumentException if the instrument does not fit in this detector.
	 * */
	public boolean isAccurate(IOpticInstrument instrument);

	/**
	 * Checks if this detector has internal focus gathering lights.
	 * */
	public boolean hasInternalFocus();

	/**
	 * Gets the internal focus in this detector.
	 * @throws UnsupportedOperationException if there is no internal focus.
	 * */
	public IOpticInstrument getInternalFocus();
}