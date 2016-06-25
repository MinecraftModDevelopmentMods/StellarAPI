package stellarapi.work.optics.position;

import stellarapi.api.lib.math.SpCoord;

/**
 * Region exists on the celestial sphere.
 * */
public interface ISpRegion {
	
	public boolean isInRegion(SpCoord coord);

}
