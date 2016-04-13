package stellarapi.render;

import stellarapi.client.ClientSettings;
import stellarapi.config.IConfigHandler;
import stellarapi.stellars.layer.CelestialObject;
import stellarapi.stellars.view.IStellarViewpoint;

public interface IRenderCache<Obj extends CelestialObject, Config extends IConfigHandler> {
	
	public void initialize(ClientSettings settings, Config specificSettings);
	public void updateCache(ClientSettings settings, Config specificSettings, Obj object, IStellarViewpoint viewpoint);
	
}
