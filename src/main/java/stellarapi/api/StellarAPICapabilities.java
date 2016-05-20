package stellarapi.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import stellarapi.api.optics.IOpticalViewer;

public class StellarAPICapabilities {
	@CapabilityInject(IOpticalViewer.class)
	public static Capability<IOpticalViewer> VIEWER_CAPABILITY = null;
}
