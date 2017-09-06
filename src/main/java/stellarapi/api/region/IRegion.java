package stellarapi.api.region;

public interface IRegion {
	/**
	 * Get larger region around this region extended by specified radius.
	 * All points within certain distance from this region will be
	 *  exclusively in the new region.
	 * */
	public IRegion getRegionInDistance(double radius);

	// TODO Region fill in details
}
