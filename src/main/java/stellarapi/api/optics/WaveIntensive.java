package stellarapi.api.optics;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import stellarapi.api.lib.math.Spmath;

/**
 * Utility class for covering wavelengths using IDW method. <p>
 * Note that this estimation method only applies to intensive properties,
 *  i.e. which are not proportional to filter's bandwidth.
 * */
public class WaveIntensive implements IWaveEstimation {
	private Map<Wavelength, Double> waveMap;
	
	public WaveIntensive(Map<Wavelength, Double> weights) {
		this.waveMap = weights;
	}
	
	@Override
	public Double apply(Wavelength wavelength) {
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
	
	@Override
	public WaveIntensive multiply(final Function<Wavelength, Double> toMultiply) {
		return new WaveIntensive(
				Maps.transformEntries(this.waveMap, new Maps.EntryTransformer<Wavelength, Double, Double>() {
			@Override
			public Double transformEntry(Wavelength key, Double value) {
				return value * toMultiply.apply(key);
			}
		}));
	}

	@Override
	public WaveIntensive divide(final Function<Wavelength, Double> toDivide) {
		return new WaveIntensive(
				Maps.transformEntries(this.waveMap, new Maps.EntryTransformer<Wavelength, Double, Double>() {
			@Override
			public Double transformEntry(Wavelength key, Double value) {
				return value / toDivide.apply(key);
			}
		}));
	}
}
