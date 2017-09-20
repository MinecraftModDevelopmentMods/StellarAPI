package stellarapi.api.celestials;

import stellarapi.api.region.IRegion;

public class CRequest {
	// TODO Celestial fill in this request
	public final IRegion region;
	public final double minIntensity;

	public CRequest(IRegion reg, double minInt) {
		this.region = reg;
		this.minIntensity = minInt;
	}
}
