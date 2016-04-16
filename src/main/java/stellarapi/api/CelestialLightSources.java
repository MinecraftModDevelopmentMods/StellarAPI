package stellarapi.api;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Celestial light source which contains main light sources of the world. <p>
 * There will be only one light source(Main star) in most case. <p>
 * But there could be some mods with multiple light sources,
 * so keep that in mind when using primary source.
 * */
public final class CelestialLightSources {
	
	private ImmutableList<ICelestialObject> objects;
	
	public CelestialLightSources(List<ICelestialObject> sources) {
		ImmutableList.copyOf(sources);
	}
	
	public ICelestialObject getPrimarySource() {
		return objects.get(0);
	}
	
	public ImmutableList<ICelestialObject> getLightSources() {
		return this.objects;
	}

}
