package stellarapi.stellars.system;

import sciapi.api.value.euclidian.EVector;
import stellarapi.render.IRenderCache;
import stellarapi.util.math.Spmath;
import stellarapi.util.math.VecMath;

public class Sun extends SolarObject {

	public Sun(boolean isRemote) {
		super(isRemote);
		this.currentMag=-26.74;
	}

	@Override
	public EVector getRelativePos(double year) {
		return null;
	}
	
	protected void updateMagnitude(EVector earthFromSun) { }

	@Override
	public IRenderCache generateCache() {
		return new SunRenderCache();
	}

	@Override
	public int getRenderId() {
		return LayerSolarSystem.sunRenderId;
	}

}
