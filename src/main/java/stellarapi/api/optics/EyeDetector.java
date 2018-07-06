package stellarapi.api.optics;

/**
 * Filter settings for naked eye. Will apply upper/lower bound for R/G/B/A, and
 * only upper bound for R/G/B. Note that processing R/G/B has various
 * limitations due to its bound, So using R/G/B/A is recommended.
 */
public class EyeDetector implements IOpticalDetector {
	/**
	 * Naked eye resolution
	 */
	public static final double DEFAULT_RESOLUTION = 0.06;

	private static final EyeDetector INSTANCE = new EyeDetector();

	public static EyeDetector getInstance() {
		return INSTANCE;
	}

}
