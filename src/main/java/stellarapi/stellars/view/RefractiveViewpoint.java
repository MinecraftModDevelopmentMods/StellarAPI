package stellarapi.stellars.view;

import net.minecraft.world.World;
import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.EProjection;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.euclidian.IEVector;
import stellarapi.common.CommonSettings;
import stellarapi.stellars.util.ExtinctionRefraction;
import stellarapi.util.math.Rotate;
import stellarapi.util.math.SpCoord;
import stellarapi.util.math.Spmath;

public class RefractiveViewpoint extends NonRefractiveViewpoint implements IStellarViewpoint {
	
	public RefractiveViewpoint(CommonSettings commonSettings, PerDimensionSettings settings) {
		super(commonSettings, settings);
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
	public double getAirmass(IValRef<EVector> vector, boolean isRefractionApplied) {
		return ExtinctionRefraction.airmass(vector, isRefractionApplied);
	}

}
