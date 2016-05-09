package stellarapi.api.optics;

/**
 * Filter settings for naked eye.
 * Will apply upper/lower bound.
 * Note that processing R/G/B has various limitations due to its bound,
 * So using R/G/B/A is recommended.
 * */
public class EyeDetector implements IOpticalDetector {
	
	private static EyeDetector INSTANCE = new EyeDetector();
	
	public static EyeDetector getInstance() {
		return INSTANCE;
	}

	@Override
	public boolean isRGB() {
		return true;
	}
	
	private static final double LOWER_LIMIT_FACTOR = 0.5;

	@Override
	public double[] process(double multiplier, IOpticalFilter filter, double[] origin) {
		if(origin.length == 3) {
			double scale = 0.0;
			double result[] = new double[3];

			for(EnumRGBA color : EnumRGBA.RGB) {
				result[color.ordinal()] = origin[color.ordinal()] * multiplier *
						filter.getFilterEfficiency(Wavelength.colorWaveMap.get(color));
				scale = Math.max(scale, result[color.ordinal()]);
			}
			
			for(EnumRGBA color : EnumRGBA.RGB)
				result[color.ordinal()] /= scale;
			
			if(scale > 1.0) {
				for(EnumRGBA color : EnumRGBA.RGB)
					result[color.ordinal()] = result[color.ordinal()] / scale + 1.0 - 1.0 / scale;
			} else for(EnumRGBA color : EnumRGBA.RGB)
				result[color.ordinal()] *= scale;

			return result;
		} else if(origin.length == 4) {
			double scale = 0.0;
			double result[] = new double[4];

			for(EnumRGBA color : EnumRGBA.RGB) {
				result[color.ordinal()] = origin[color.ordinal()] * filter.getFilterEfficiency(Wavelength.colorWaveMap.get(color));
				scale = Math.max(scale, result[color.ordinal()]);
			}
			
			for(EnumRGBA color : EnumRGBA.RGB)
				result[color.ordinal()] /= scale;
			
			result[3] = origin[3] * scale * multiplier;
			scale = result[3];
			
			if(scale > 1.0) {
				for(EnumRGBA color : EnumRGBA.RGB)
					result[color.ordinal()] = result[color.ordinal()] / scale + 1.0 - 1.0 / scale;
			} else if(scale < LOWER_LIMIT_FACTOR) {
				for(EnumRGBA color : EnumRGBA.RGB)
					result[color.ordinal()] = result[color.ordinal()] * 2 * scale + 1.0 - 2 * scale;
			}

			return result;
		} else throw new IllegalArgumentException("RGB filter can only process R/G/B or R/G/B/A.");
	}

}
