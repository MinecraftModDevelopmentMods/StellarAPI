package stellarapi.api.celestials;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Main effectors which influences major effects to the world. <p>
 * There will be only one effector in most case. <p>
 * But there could be some mods with multiple effectors,
 * so keep that in mind when using primary source.
 * */
public final class CelestialEffectors {
	
	private ImmutableList<ICelestialObject> objects;
	
	public CelestialEffectors(List<ICelestialObject> effectors) {
		ImmutableList.copyOf(effectors);
	}
	
	public ICelestialObject getPrimarySource() {
		return objects.get(0);
	}
	
	public ImmutableList<ICelestialObject> getLightSources() {
		return this.objects;
	}

}
