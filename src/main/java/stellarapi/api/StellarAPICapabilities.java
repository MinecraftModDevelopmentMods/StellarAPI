package stellarapi.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import stellarapi.api.interact.IOpticalProperties;
import stellarapi.api.optics.IOpticalViewer;

public class StellarAPICapabilities {
	@CapabilityInject(IOpticalViewer.class)
	public static final Capability<IOpticalViewer> VIEWER_CAPABILITY = null;
	
	@CapabilityInject(IOpticalProperties.class)
	public static final Capability<IOpticalProperties> OPTICAL_PROPERTY = null;
}