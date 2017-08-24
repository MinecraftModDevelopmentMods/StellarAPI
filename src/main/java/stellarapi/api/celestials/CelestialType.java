package stellarapi.api.celestials;

import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

/**
 * Certain celestial type.
 * */
public class CelestialType extends IForgeRegistryEntry.Impl<CelestialType> {

	private final CelestialType parent;

	public CelestialType() {
		this.parent = null;
		// TODO add coordinate and systems here
	}

	public CelestialType(CelestialType parent) {
		this.parent = parent;
	}

}