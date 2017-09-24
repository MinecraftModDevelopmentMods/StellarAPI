package stellarapi.api.optics;

/**
 * Represents certain range of wavelength.
 * */
public class WaveRange {
	//range points in nm
	public final double shortLen, longLen;

	public WaveRange(double begin, double end) {
		this.shortLen = begin;
		this.longLen = end;
	}

	public double getCenter() {
		return (this.shortLen + this.longLen) * 0.5;
	}
}