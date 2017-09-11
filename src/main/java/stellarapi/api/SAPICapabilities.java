package stellarapi.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import stellarapi.api.coordinates.ICoordSystem;
import stellarapi.api.coordinates.ILocalCoordinates;
import stellarapi.api.interact.IOpticalProperties;
import stellarapi.api.optics.IOpticalViewer;

public class SAPICapabilities {

	@CapabilityInject(ILocalCoordinates.class)
	public static final Capability<ILocalCoordinates> LOCAL_COORDINATES = null;

	@CapabilityInject(ICoordSystem.class)
	public static final Capability<ICoordSystem> COORDINATES_SYSTEM = null;


	//@CapabilityInject(IOpticalViewer.class)
	//public static final Capability<IOpticalViewer> VIEWER_CAPABILITY = null;
	
	//@CapabilityInject(IOpticalProperties.class)
	//public static final Capability<IOpticalProperties> OPTICAL_PROPERTY = null;
}