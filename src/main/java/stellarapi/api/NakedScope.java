package stellarapi.api;

import stellarapi.api.wavecolor.Wavelength;

public class NakedScope implements IViewScope {

	@Override
	public double getLGP() {
		return 1.0;
	}

	@Override
	public double getResolution(Wavelength wl) {
		return 0.3;
	}

	@Override
	public double getMP() {
		return 1.0;
	}

	@Override
	public boolean isFOVCoverSky() {
		return true;
	}

}
