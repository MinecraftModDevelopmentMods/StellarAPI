package stellarapi.internal.atmosphere;

import stellarapi.api.atmosphere.Atmosphere;
import stellarapi.api.atmosphere.IAtmHolder;
import stellarapi.api.atmosphere.ILocalAtmosphere;

/**
 * Generic atmosphere, usually for celestial objects
 * */
public class CGenericAtmosphere implements IAtmHolder {
	private Atmosphere atmosphere;

	@Override
	public Atmosphere getAtmosphere() { return this.atmosphere; }
	@Override
	public void setAtmosphere(Atmosphere atm) { this.atmosphere = atm; }
	@Override
	public ILocalAtmosphere getLocalAtmosphere() { return null; }
	@Override
	public void reevaluateLocalAtmosphere() { }

}
