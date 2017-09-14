package stellarapi.api.atmosphere;

import stellarapi.api.optics.Wavelength;

public class AtmosphereLayer {
	// TODO Atmosphere Correctly define each layer.

	private double densityScaleHeight;
	private double[] densityCharacteristics;

	public double getAbsorption(Wavelength wave) {
		return 0.0;
	}

	public double getScatterRate(Wavelength wave, double angle) {
		return 0.0;
	}
}