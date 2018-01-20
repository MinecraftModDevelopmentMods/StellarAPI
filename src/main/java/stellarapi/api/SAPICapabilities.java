package stellarapi.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import stellarapi.api.interact.IOpticalProperties;
import stellarapi.api.optics.IOpticalViewer;
import stellarapi.api.world.ICelestialWorld;

public class SAPICapabilities {
	@CapabilityInject(ICelestialWorld.class)
	public static final Capability<ICelestialWorld> CELESTIAL_CAPABILITY = null;


	@CapabilityInject(IOpticalViewer.class)
	public static final Capability<IOpticalViewer> VIEWER_CAPABILITY = null;
	
	@CapabilityInject(IOpticalProperties.class)
	public static final Capability<IOpticalProperties> OPTICAL_PROPERTY = null;
}