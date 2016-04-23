package stellarapi.api.optics;

import com.google.common.base.Function;

/**
 * Utility interface for wave estimations.
 * */
public interface IWaveEstimation extends Function<Wavelength, Double> {
	
	/**
	 * Multiplies the estimation.
	 * @param toMultiply function to multiply
	 * */
	public IWaveEstimation multiply(final Function<Wavelength, Double> toMultiply);
	
	/**
	 * Divides the estimation.
	 * @param toDivide function to divide, this will be the divisor.
	 * */
	public IWaveEstimation divide(final Function<Wavelength, Double> toDivide);

}
