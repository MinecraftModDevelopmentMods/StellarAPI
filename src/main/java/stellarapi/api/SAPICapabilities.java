package stellarapi.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class SAPICapabilities {
	@CapabilityInject(ICelestialWorld.class)
	public static final Capability<ICelestialWorld> CELESTIAL_CAPABILITY = null;
}