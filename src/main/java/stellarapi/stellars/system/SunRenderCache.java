package stellarapi.stellars.system;

import sciapi.api.value.euclidian.EVector;
import stellarapi.client.ClientSettings;
import stellarapi.config.IConfigHandler;
import stellarapi.render.IRenderCache;
import stellarapi.stellars.view.IStellarViewpoint;
import stellarapi.util.math.SpCoord;
import stellarapi.util.math.Spmath;
import stellarapi.util.math.VecMath;

public class SunRenderCache implements IRenderCache<Sun, IConfigHandler> {
	
	protected SpCoord appCoord = new SpCoord();
	protected double size;

	@Override
	public void initialize(ClientSettings settings, IConfigHandler config) { }

	@Override
	public void updateCache(ClientSettings settings, IConfigHandler config, Sun object, IStellarViewpoint viewpoint) {
		EVector ref = new EVector(3);
		ref.set(viewpoint.getProjection().transform(object.earthPos));
		appCoord.setWithVec(ref);
		viewpoint.applyAtmRefraction(this.appCoord);
		
		this.size = object.radius / Spmath.getD(VecMath.size(object.earthPos))*99.0*20;
	}

}
