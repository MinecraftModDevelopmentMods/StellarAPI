package stellarapi.api.instruments;

/**
 * Interface for optic instrument like mirrors and lenses.
 * */
public interface IOpticInstrument {
	// TODO OpticalInstr fill in these
	/**
	 * Effective diameter in millimeters.
	 * e.g. eye has diameter of 5mm.
	 * */
	public double effectiveDiameter();

	/**
	 * Magnification factor.
	 * What this means is dependent on its type.
	 * 
	 * It's scale factor for focus,
	 * magnification factor for scope,
	 * 1/(reverse scale factor) for disperse,
	 * ratio of image size for propagate.
	 * */
	public double magFactor();

	/**
	 * Checks if this instrument is mirror or not.
	 * Mirror reflects the image.
	 * */
	public boolean isMirror();

	/**
	 * Gets the instrument type.
	 * */
	public EnumOpticInstrType getType();
}
