package stellarapi.api.optics;

/**
 * Scope settings for naked eye.
 * */
public class NakedScope implements IViewScope {
	
	/**
	 * Default naked eye resolution.
	 * */
	public static final double DEFAULT_RESOLUTION = 0.06;

	@Override
	public double getLGP() {
		return 1.0;
	}

	@Override
	public double getResolution(Wavelength wl) {
		return DEFAULT_RESOLUTION;
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
