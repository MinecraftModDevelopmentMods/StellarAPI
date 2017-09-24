package stellarapi.api.optics;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import scala.actors.threadpool.Arrays;

/**
 * General filter. Allows transmission on various wave types.
 * It's recommended to have only one wave filter type.
 * */
public class OpFilter {

	public final ImmutableMap<WaveFilterType, Double> transmissivityMap;

	public OpFilter(ImmutableMap<WaveFilterType, Double> theMap) {
		this.transmissivityMap = theMap;
	}

	public OpFilter(Map.Entry<WaveFilterType, Double>... entries) {
		this(ImmutableMap.copyOf(Arrays.asList(entries)));
	}
}