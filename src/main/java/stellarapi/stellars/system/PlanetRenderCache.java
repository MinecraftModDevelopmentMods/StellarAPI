package stellarapi.stellars.system;

import sciapi.api.value.euclidian.EVector;
import stellarapi.client.ClientSettings;
import stellarapi.config.IConfigHandler;
import stellarapi.render.IRenderCache;
import stellarapi.stellars.Optics;
import stellarapi.stellars.view.IStellarViewpoint;
import stellarapi.util.math.SpCoord;

public class PlanetRenderCache implements IRenderCache<SolarObject, IConfigHandler> {
	
	protected boolean shouldRender;
	protected SpCoord appCoord = new SpCoord();
	protected float appMag;
	
	@Override
	public void initialize(ClientSettings settings, IConfigHandler config) { }

	@Override
	public void updateCache(ClientSettings settings, IConfigHandler config, SolarObject object, IStellarViewpoint viewpoint) {
		EVector ref = new EVector(3);
		ref.set(viewpoint.getProjection().transform(object.earthPos));
		double airmass = viewpoint.getAirmass(ref, false);
		this.appMag = (float) (object.currentMag + airmass * Optics.ext_coeff_V);
		
		this.shouldRender = true;
		if(this.appMag > settings.mag_Limit)
		{
			this.shouldRender = false;
			return;
		}
		
		appCoord.setWithVec(ref);
		viewpoint.applyAtmRefraction(this.appCoord);
		
		if(appCoord.y < 0.0 && viewpoint.hideObjectsUnderHorizon())
			this.shouldRender = false;
	}

}
