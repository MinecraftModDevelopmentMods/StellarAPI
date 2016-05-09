package stellarapi.api.optics;

public class NakedFilter implements IOpticalFilter {

	@Override
	public double getFilterEfficiency(Wavelength wavelength) {
		return 1.0;
	}

}
