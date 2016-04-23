package stellarapi.api.optics;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import stellarapi.api.lib.math.Spmath;

/**
 * Utility class for covering wavelengths. <p>
 * Note that this estimation method only applies to extensive properties,
 *  i.e. which are proportional to filter's bandwidth.
 * */
public class WaveExtensive implements IWaveEstimation {
	private Map<Wavelength, Double> waveMap;
	
	public WaveExtensive(Map<Wavelength, Double> weights) {
		this.waveMap = weights;
	}
	
	public Double apply(Wavelength wavelength) {
		double res = 0.0;
		double weightedSum = 0.0;
		double width = wavelength.getWidth();
		
		if(waveMap.containsKey(wavelength))
			return waveMap.get(wavelength);
		
		for(Map.Entry<Wavelength, Double> entry : waveMap.entrySet()) {
			if(entry.getKey().getLength() == wavelength.getLength())
				return entry.getValue() * width / entry.getKey().getWidth();
			
			double weight = Spmath.quad(entry.getKey().getWidth() / (entry.getKey().getLength() - wavelength.getLength()));
			res += entry.getValue() / entry.getKey().getWidth() * weight;
			weightedSum += weight;
		}
		
		return res / weightedSum * width;
	}

	@Override
	public WaveExtensive multiply(final Function<Wavelength, Double> toMultiply) {
		return new WaveExtensive(
				Maps.transformEntries(this.waveMap, new Maps.EntryTransformer<Wavelength, Double, Double>() {
			@Override
			public Double transformEntry(Wavelength key, Double value) {
				return value * toMultiply.apply(key);
			}
		}));
	}

	@Override
	public WaveExtensive divide(final Function<Wavelength, Double> toDivide) {
		return new WaveExtensive(
				Maps.transformEntries(this.waveMap, new Maps.EntryTransformer<Wavelength, Double, Double>() {
			@Override
			public Double transformEntry(Wavelength key, Double value) {
				return value / toDivide.apply(key);
			}
		}));
	}
}
