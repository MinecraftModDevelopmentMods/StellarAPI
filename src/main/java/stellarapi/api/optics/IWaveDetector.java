package stellarapi.api.optics;

import java.awt.Color;

public interface IWaveDetector {

	/**Detector Wavelength, should be constant*/
	public Wavelength getWavelength();

	/**Detector Efficiency (1.0 means the best efficiency)*/
	public double getDetectorEfficiency();

	/** Output Color */
	public Color getColor();

}
