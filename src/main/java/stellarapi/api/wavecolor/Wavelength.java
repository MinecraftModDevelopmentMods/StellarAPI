package stellarapi.api.wavecolor;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * Wavelength type to describe wave property of optical filter.
 * @see <a href="https://en.wikipedia.org/wiki/Photometric_system">Photometric system</a>
 * */
public class Wavelength {
	
	/**
	 * Normal visible wavelength for (naked) eye.
	 * */
	public static final Wavelength visible = new Wavelength(551, 300);
	
	/**
	 * Wavelength for U filter. (Lies on ultraviolet, but still has big importance)
	 * */
	public static final Wavelength U = new Wavelength(365, 66);
	
	/**
	 * Wavelength for B filter.
	 * Common wavelength for blue color.
	 * */
	public static final Wavelength B = new Wavelength(445, 94);
	
	/**
	 * Wavelength for V filter.
	 * In normal case, share the same property with visible wavelength.
	 * Also nearly common wavelength for green color.
	 * */
	public static final Wavelength V = new Wavelength(551, 88);
	
	/**
	 * Wavelength for R filter.
	 * Common wavelength for red color. (Does not have big importance as filter)
	 * */
	public static final Wavelength R = new Wavelength(658, 138);
	
	public static final ImmutableMap<EnumRGBA, Wavelength> colorWaveMap = Maps.immutableEnumMap(
			ImmutableMap.<EnumRGBA, Wavelength>of(
					EnumRGBA.Red, R, EnumRGBA.Green, V,
					EnumRGBA.Blue, B, EnumRGBA.Alpha, visible));
	
	/**
	 * Wavelength in nm.
	 * */
	private final double length;
	
	/**
	 * Bandwidth(FWHM) in nm.
	 * */
	private final double bandwidth;
	
	public Wavelength(double length, double bandwidth) {
		this.length = length;
		this.bandwidth = bandwidth;
	}
	
	public double getLength() {
		return this.length;
	}
	
	public double getWidth() {
		return this.bandwidth;
	}
	
	@Override
	public int hashCode() {
		long bits = Double.doubleToLongBits(this.length);
	    long bits2 = Double.doubleToLongBits(this.bandwidth);
	    return (int)(bits ^ bits2);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Wavelength)
			return ((Wavelength) obj).length == this.length 
			&& ((Wavelength) obj).bandwidth == this.bandwidth;
		else return false;
	}
	
	/**
	 * Utility class for covering wavelengths. <p>
	 * Note that this interpolation method does not apply to the properties
	 *  which are proportional to filter's bandwidth. In those case, scaling is needed.
	 * */
	public static class WaveInterpolation {
		private Wavelength[] waves;
		private double[] weights;
		
		public WaveInterpolation(Wavelength[] wavelengths, double[] weights) {
			if(wavelengths.length != weights.length)
				throw new IllegalArgumentException(
						"Two parameters must have same size!");
			
			this.waves = wavelengths;
			this.weights = weights;
		}
		
		public double getInterpolated(Wavelength wavelength) {
			double res = 0.0;
			double weightedSum = 0.0;
			for(int i = 0; i < waves.length; i++) {
				if(waves[i].length == wavelength.length)
					return weights[i];
				
				double weight = waves[i].bandwidth / (waves[i].length - wavelength.length);
				res += weights[i] * weight;
				weightedSum += weight;
			}
			
			return res / weightedSum;
		}
	}
}
