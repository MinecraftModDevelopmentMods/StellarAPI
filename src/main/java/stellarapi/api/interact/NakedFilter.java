package stellarapi.api.interact;

import stellarapi.api.optics.Wavelength;

public class NakedFilter implements IFilter {

	@Override
	public float transformQE(Wavelength wavelength, float prevEfficiency) {
		return prevEfficiency;
	}

}
