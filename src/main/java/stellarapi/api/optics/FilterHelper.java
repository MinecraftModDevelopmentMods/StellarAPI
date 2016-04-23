package stellarapi.api.optics;

import com.google.common.collect.ImmutableMap;

public class FilterHelper {
	
	/**
	 * Get filtered RGB value with interpolation.
	 * @param filter the optical filter
	 * @param rgb the unfiltered RGB value
	 * */
	public static double[] getFilteredRGB(IOpticalFilter filter, double[] rgb) {
		if(filter.isRGB()) {
			return new double[] {
				rgb[0] * filter.getFilterList().get(0).getFilterEfficiency(),
				rgb[1] * filter.getFilterList().get(1).getFilterEfficiency(),
				rgb[2] * filter.getFilterList().get(2).getFilterEfficiency()
			};
		} else {
			double[] result = new double[] {0, 0, 0};
			WaveExtensive interpolation = new WaveExtensive(
					ImmutableMap.of(Wavelength.R, rgb[0],
							Wavelength.V, rgb[1],
							Wavelength.B, rgb[2]));
			
			for(IWaveFilter wfilter : filter.getFilterList()) {
				double value = interpolation.estimate(wfilter.getWavelength()) * wfilter.getFilterEfficiency();
				result[0] += value * wfilter.getColor().getRed() / 255.0;
				result[1] += value * wfilter.getColor().getGreen() / 255.0;
				result[2] += value * wfilter.getColor().getBlue() / 255.0;
			}
			
			return result;
		}
	}
	
	private static final double LIMIT_FACTOR = 2.0;
	
	/**
	 * Get filtered RGB value with interpolation, which is bounded by 1.0
	 * @param filter the optical filter
	 * @param rgb the unfiltered RGB value
	 * */
	public static double[] getFilteredRGBBounded(IOpticalFilter filter, double[] rgb) {
		if(filter.isRGB()) {
			double scale = 0.0;
			double result[] = new double[3];
			
			for(EnumRGBA color : EnumRGBA.RGB)
			{
				result[color.ordinal()] = rgb[color.ordinal()] *
						filter.getFilterList().get(color.ordinal()).getFilterEfficiency();
				scale += result[color.ordinal()] / 3.0;
			}
			
			scale = Math.min(scale, LIMIT_FACTOR);
			scale = Math.max(1.0, scale);
			
			for(EnumRGBA color : EnumRGBA.RGB)
				result[color.ordinal()] = Math.min(
						result[color.ordinal()] / scale, 1.0);
			
			return result;
		} else {
			double[] result = new double[] {0, 0, 0};
			double scale = 0.0;
			
			WaveExtensive interpolation = new WaveExtensive(
					ImmutableMap.of(Wavelength.R, rgb[0],
							Wavelength.V, rgb[1],
							Wavelength.B, rgb[2]));
			
			for(IWaveFilter wfilter : filter.getFilterList()) {
				double brightnessForWave = interpolation.estimate(wfilter.getWavelength());
				result[0] += brightnessForWave * wfilter.getColor().getRed() / 255.0;
				result[1] += brightnessForWave * wfilter.getColor().getGreen() / 255.0;
				result[2] += brightnessForWave * wfilter.getColor().getBlue() / 255.0;
			}
			
			for(EnumRGBA color : EnumRGBA.RGB)
				scale += result[color.ordinal()] / 3.0;
			
			scale = Math.min(scale, LIMIT_FACTOR);
			scale = Math.max(scale, 1.0);
			
			for(EnumRGBA color : EnumRGBA.RGB)
				result[color.ordinal()] = Math.min(
						result[color.ordinal()] / scale, 1.0);
			
			return result;
		}
	}
	
	
	/**
	 * Get filtered RGB value with interpolation.
	 * @param filter the optical filter
	 * @param brinfo the brightness information on certain wavelength
	 * */
	public static double[] getFilteredRGB(IOpticalFilter filter, WaveExtensive brinfo) {
		if(filter.isRGB()) {
			return new double[] {
				brinfo.estimate(Wavelength.R) * filter.getFilterList().get(0).getFilterEfficiency(),
				brinfo.estimate(Wavelength.V) * filter.getFilterList().get(1).getFilterEfficiency(),
				brinfo.estimate(Wavelength.B) * filter.getFilterList().get(2).getFilterEfficiency()
			};
		} else {
			double[] result = new double[] {0, 0, 0};
			
			for(IWaveFilter wfilter : filter.getFilterList()) {
				double value = brinfo.estimate(wfilter.getWavelength()) * wfilter.getFilterEfficiency();
				result[0] += value * wfilter.getColor().getRed() / 255.0;
				result[1] += value * wfilter.getColor().getGreen() / 255.0;
				result[2] += value * wfilter.getColor().getBlue() / 255.0;
			}
			
			return result;
		}
	}
	
	/**
	 * Get filtered RGB value with interpolation, which is bounded by 1.0
	 * @param filter the optical filter
	 * @param brinfo the brightness information on certain wavelength
	 * */
	public static double[] getFilteredRGBBounded(IOpticalFilter filter, WaveExtensive brinfo) {
		if(filter.isRGB()) {
			double scale = 0.0;
			double result[] = new double[3];
			
			for(EnumRGBA color : EnumRGBA.RGB)
			{
				result[color.ordinal()] = brinfo.estimate(Wavelength.colorWaveMap.get(color)) *
						filter.getFilterList().get(color.ordinal()).getFilterEfficiency();
				scale += result[color.ordinal()] / 3.0;
			}
			
			scale = Math.min(scale, LIMIT_FACTOR);
			scale = Math.max(1.0, scale);
			
			for(EnumRGBA color : EnumRGBA.RGB)
				result[color.ordinal()] = Math.min(
						result[color.ordinal()] / scale, 1.0);
			
			return result;
		} else {
			double[] result = new double[] {0, 0, 0};
			double scale = 0.0;
			
			for(IWaveFilter wfilter : filter.getFilterList()) {
				double brightnessForWave = brinfo.estimate(wfilter.getWavelength()) * wfilter.getFilterEfficiency();
				result[0] += brightnessForWave * wfilter.getColor().getRed() / 255.0;
				result[1] += brightnessForWave * wfilter.getColor().getGreen() / 255.0;
				result[2] += brightnessForWave * wfilter.getColor().getBlue() / 255.0;
			}
			
			for(EnumRGBA color : EnumRGBA.RGB)
				scale += result[color.ordinal()] / 3.0;
			
			scale = Math.min(scale, LIMIT_FACTOR);
			scale = Math.max(scale, 1.0);
			
			for(EnumRGBA color : EnumRGBA.RGB)
				result[color.ordinal()] = Math.min(
						result[color.ordinal()] / scale, 1.0);
			
			return result;
		}
	}

}
