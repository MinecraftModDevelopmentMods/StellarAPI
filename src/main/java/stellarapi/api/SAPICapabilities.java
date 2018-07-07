package stellarapi.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import stellarapi.api.interact.IFilter;
import stellarapi.api.interact.IScope;
import stellarapi.api.world.ICelestialWorld;

public class SAPICapabilities {
	@CapabilityInject(ICelestialWorld.class)
	public static final Capability<ICelestialWorld> CELESTIAL_CAPABILITY = null;


	@CapabilityInject(IScope.class)
	public static final Capability<IScope> SCOPE_CAPABILITY = null;

	@CapabilityInject(IFilter.class)
	public static final Capability<IFilter> FILTER_CAPABILITY = null;
}