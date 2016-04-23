package stellarapi.api.optics;

import java.util.Map;

import stellarapi.api.lib.math.Spmath;

/**
 * Utility class for covering wavelengths. <p>
 * Note that this estimation method only applies to extensive properties,
 *  i.e. which are proportional to filter's bandwidth.
 * */
public class WaveExtensive {
	private Map<Wavelength, Double> waveMap;
	
	public WaveExtensive(Map<Wavelength, Double> weights) {
		this.waveMap = weights;
	}
	
	public double estimate(Wavelength wavelength) {
		double res = 0.0;
		double weightedSum = 0.0;
		double width = wavelength.getWidth();
		
		if(waveMap.containsKey(wavelength))
			return waveMap.get(wavelength);
		
		for(Map.Entry<Wavelength, Double> entry : waveMap.entrySet()) {
			if(entry.getKey().getLength() == wavelength.getLength())
				return entry.getValue() * width / entry.getKey().getWidth();
			
			double weight = Spmath.quad(entry.getKey().getWidth() / (entry.getKey().getLength() - wavelength.getLength()))
					/ entry.getKey().getWidth();
			res += entry.getValue() * weight;
			weightedSum += weight;
		}
		
		return res / weightedSum * width;
	}
}
