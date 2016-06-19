package stellarapi.api.optics;

/**
 * Helper for detectors, this should be used by the renderer.
 */
public interface IOpticalDetector {

	/**
	 * <code>true</code> for RGB type detectors. RGB type detectors will support
	 * R/G/B and R/G/B/A for processing, Where R,G,B are colors bounded in [0.0,
	 * 1.0] and A is alpha in R/G/B/A, (Alpha is not bounded) and R,G,B are
	 * brightness bounded in [0.0, 1.0] in R/G/B.
	 */
	public boolean isRGB();

	/**
	 * Process the amount of light through the scopes and filters. A detector
	 * can apply specific effects here. The resulted colors will be bounded in
	 * [0.0, 1.0].
	 * 
	 * @param multiplier
	 *            brightness multiplier calculated from scope for specific
	 *            object
	 * @param filter
	 *            the optical filter
	 * @param origin
	 *            the origin value in R/G/B or R/G/B/A format, will throw
	 *            <code>IllegalArgumentException</code> if it is not.
	 */
	public double[] process(double multiplier, IOpticalFilter filter, double[] origin);

}
