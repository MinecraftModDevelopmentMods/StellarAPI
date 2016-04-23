package stellarapi.api.optics;

import java.util.Map;

import stellarapi.api.lib.math.Spmath;

/**
 * Utility class for covering wavelengths using IDW method. <p>
 * Note that this estimation method only applies to intensive properties,
 *  i.e. which are not proportional to filter's bandwidth.
 * */
public class WaveIntensive {
	private Map<Wavelength, Double> waveMap;
	
	public WaveIntensive(Map<Wavelength, Double> weights) {
		this.waveMap = weights;
	}
	
	public double estimate(Wavelength wavelength) {
		double res = 0.0;
		double weightedSum = 0.0;
		for(Map.Entry<Wavelength, Double> entry : waveMap.entrySet()) {
			if(entry.getKey().getLength() == wavelength.getLength())
				return entry.getValue();
			
			double weight = Spmath.quad(entry.getKey().getWidth() / (entry.getKey().getLength() - wavelength.getLength()));
			res += entry.getValue() * weight;
			weightedSum += weight;
		}
		
		return res / weightedSum;
	}
}
