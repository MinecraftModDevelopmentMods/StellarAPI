package stellarium.world;

import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.optics.WaveFilterType;
import stellarium.stellars.util.ExtinctionRefraction;

public class RefractiveSkySet extends NonRefractiveSkySet implements IStellarSkySet {
	
	public RefractiveSkySet(PerDimensionSettings settings) {
		super(settings);
	}

	@Override
	public void applyAtmRefraction(SpCoord coord) {
		ExtinctionRefraction.refraction(coord, true);
	}

	@Override
	public void disapplyAtmRefraction(SpCoord coord) {
		ExtinctionRefraction.refraction(coord, false);
	}

	@Override
	public float calculateAirmass(SpCoord coord) {
		return (float)ExtinctionRefraction.airmass(coord, false);
	}
	
	@Override
	public double getSeeing(WaveFilterType wavelength) {
		return 0.002;
	}

}
