package stellarapi.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import stellarapi.api.celestials.ICelestialUniverse;
import stellarapi.api.interact.IOpticalProperties;
import stellarapi.api.optics.IOpticalViewer;

public class StellarAPICapabilities {
	@CapabilityInject(IOpticalViewer.class)
	public static final Capability<IOpticalViewer> VIEWER_CAPABILITY = null;
	
	@CapabilityInject(IOpticalProperties.class)
	public static final Capability<IOpticalProperties> OPTICAL_PROPERTY = null;


	@CapabilityInject(ICelestialUniverse.class)
	public static final Capability<ICelestialUniverse> CELESTIAL_CAPABILITY = null;

	@CapabilityInject(ICelestialCoordinate.class)
	public static final Capability<ICelestialCoordinate> COORDINATE_CAPABILITY = null;

	@CapabilityInject(ISkyEffect.class)
	public static final Capability<ISkyEffect> SKYEFFECT_CAPABILITY = null;
}