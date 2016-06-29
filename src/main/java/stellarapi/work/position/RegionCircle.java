package stellarapi.work.position;

import stellarapi.api.lib.math.SpCoord;

public class RegionCircle implements ISpRegion {
	
	private SpCoord center;
	private double radius;

	@Override
	public boolean isInRegion(SpCoord coord) {
		return center.distanceTo(coord) < this.radius;
	}

}