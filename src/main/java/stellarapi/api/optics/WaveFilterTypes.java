package stellarapi.api.optics;

public class WaveFilterTypes {
	/**
	 * Normal visible wavelength for (naked) eye.
	 */
	public static final WaveFilterType visible = new WaveFilterType(498, 200);


	/**
	 * Wavelength for U filter. (Lies on ultraviolet, but still has big importance)
	 */
	public static final WaveFilterType U = new WaveFilterType(365, 66);

	/**
	 * Wavelength for B filter. Common wavelength for blue color.
	 */
	public static final WaveFilterType B = new WaveFilterType(445, 94);

	/**
	 * Wavelength for V filter. In normal case, share the same property with
	 * visible wavelength. Also nearly common wavelength for green color.
	 */
	public static final WaveFilterType V = new WaveFilterType(551, 88);

	/**
	 * Wavelength for red color.
	 */
	public static final WaveFilterType red = new WaveFilterType(658, 90);

	/**
	 * Wavelength for R filter.
	 */
	public static final WaveFilterType R = new WaveFilterType(658, 138);
}
