package stellarapi.api.region;

import stellarapi.api.lib.math.SpCoord;

public interface IRegion {
	/**
	 * Get larger region around this region extended by specified radius.
	 * All points within certain distance from this region will be
	 *  exclusively in the new region.
	 * */
	public IRegion getRegionInDistance(double radius);

	/**
	 * Checks if certain coordinates is in the region.
	 * */
	public boolean isInRegion(SpCoord coord);

	// TODO Region fill in details
}
