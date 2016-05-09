package stellarapi.feature.horseriding;

import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.Wavelength;

public class ScopeHorseRiding implements IViewScope {
	
	private IViewScope previous;
	
	public ScopeHorseRiding(IViewScope previous) {
		this.previous = previous;
	}

	@Override
	public double getLGP() {
		return previous.getLGP() * 0.7;
	}

	@Override
	public double getResolution(Wavelength wl) {
		return previous.getResolution(wl);
	}

	@Override
	public double getMP() {
		return previous.getMP() * 0.8;
	}

	@Override
	public boolean forceChange() {
		return previous.forceChange();
	}

	@Override
	public boolean isFOVCoverSky() {
		return previous.isFOVCoverSky();
	}

}
