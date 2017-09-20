package stellarapi.internal.atmosphere;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.atmosphere.Atmosphere;
import stellarapi.api.atmosphere.IAtmHolder;
import stellarapi.api.atmosphere.IAtmSetProvider;
import stellarapi.api.atmosphere.ILocalAtmosphere;

/**
 * Generic atmosphere, usually for celestial objects.
 * Does not support loading/saving.
 * */
public class CGenericAtmosphere implements IAtmHolder {
	private Atmosphere atmosphere = null;

	@Override
	public Atmosphere getAtmosphere() { return this.atmosphere; }
	@Override
	public void setAtmosphere(Atmosphere atm) { this.atmosphere = atm; }

	@Override
	public ILocalAtmosphere getLocalAtmosphere() { return null; }

	@Override
	public void setProviderID(ResourceLocation providerID) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void evaluateAtmosphere(Object atmSettings) {
		throw new UnsupportedOperationException();
	}
	@Override
	public ResourceLocation getProviderID() {
		throw new UnsupportedOperationException();
	}
	@Override
	public IAtmSetProvider getSetProvider() {
		throw new UnsupportedOperationException();
	}

}