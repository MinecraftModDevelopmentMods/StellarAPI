package stellarapi.api.optics;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * Filter per wavelength type to describe wave property of optical filter.
 * This should be centralized around the specific wavelength.
 * By default, it's considered as normal distribution.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Photometric_system">Photometric
 *      system</a>
 */
public class WaveFilterType {
	/**
	 * Center wavelength in nm.
	 */
	public final double length;

	/**
	 * Bandwidth(FWHM) in nm.
	 */
	public final double bandwidth;

	public WaveFilterType(double length, double bandwidth) {
		this.length = length;
		this.bandwidth = bandwidth;
	}

	/**
	 * Gets the relative efficiency on certain short range of wavelength.
	 * The maximal efficiency of this filter type is, relatively, 1.0.
	 * */
	public double getEfficiency(WaveRange wave) {
		// TODO Wave fill in here & Registrable?
		return 0.0;
	}

	/**
	 * Gets the central range where the efficiency is close to 1.0.
	 * */
	public WaveRange getCentralRange() {
		return new WaveRange(this.length - this.bandwidth * 0.1, this.length + this.bandwidth * 0.1);
	}

	/**
	 * Gets the cover range where the efficiency is meaningfully bigger than zero.
	 * */
	public WaveRange getCoverRange() {
		return new WaveRange(this.length - this.bandwidth, this.length + this.bandwidth);
	}

	@Override
	public int hashCode() {
		long bits = Double.doubleToLongBits(this.length);
		long bits2 = Double.doubleToLongBits(this.bandwidth);
		return (int) ((bits ^ bits2) >> 32);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WaveFilterType)
			return ((WaveFilterType) obj).length == this.length && ((WaveFilterType) obj).bandwidth == this.bandwidth;
		else
			return false;
	}
}
