package stellarapi.api.celestials;
import java.util.EnumMap;

import com.google.common.collect.Maps;

/**
 * Manager for celestial effectors which influences major effects to the world. <p>
 * 
 * */
public final class CelestialEffectorManager {
	
	private EnumMap<EnumEffectorType, CelestialEffectors> effectors = Maps.newEnumMap(EnumEffectorType.class);
	
	public CelestialEffectors getEffectors(EnumEffectorType type) {
		return effectors.get(type);
	}

}
