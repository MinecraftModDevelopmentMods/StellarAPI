package stellarapi.api.optics;

/**
 * Scope settings for naked eye.
 * */
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
	public boolean forceChange() {
		return false;
	}

	@Override
	public boolean isFOVCoverSky() {
		return true;
	}

}
