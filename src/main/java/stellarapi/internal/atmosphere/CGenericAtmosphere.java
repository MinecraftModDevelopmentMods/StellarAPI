package stellarapi.internal.atmosphere;

import stellarapi.api.atmosphere.Atmosphere;
import stellarapi.api.atmosphere.IAtmHolder;
import stellarapi.api.atmosphere.ILocalAtmosphere;

/**
 * Generic atmosphere, usually for celestial objects.
 * Does not support loading/saving.
 * */
public class CGenericAtmosphere implements IAtmHolder {
	private Atmosphere atmosphere = null;
	private boolean isSetup = false;

	@Override
	public Atmosphere getAtmosphere() { return this.atmosphere; }
	@Override
	public void setAtmosphere(Atmosphere atm) { this.atmosphere = atm; this.isSetup = true; }
	@Override
	public boolean isAtmosphereSetup() { return this.isSetup; }

	@Override
	public ILocalAtmosphere getLocalAtmosphere() { return null; }
	@Override
	public void reevaluateAtmosphere(Object atmSettings) { throw new UnsupportedOperationException(); }

}