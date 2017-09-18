package stellarapi.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import stellarapi.api.atmosphere.IAtmHolder;
import stellarapi.api.atmosphere.IAtmSystem;
import stellarapi.api.celestials.ICelestialScene;
import stellarapi.api.celestials.ICelestialSystem;
import stellarapi.api.coordinates.ICoordSystem;
import stellarapi.api.coordinates.ILocalCoordinates;

public class SAPICapabilities {
	// ********************************************* //
	// ********* Coordinates Capabilities ********** //
	// ********************************************* //

	@CapabilityInject(ILocalCoordinates.class)
	public static final Capability<ILocalCoordinates> LOCAL_COORDINATES = null;

	@CapabilityInject(ICoordSystem.class)
	public static final Capability<ICoordSystem> COORDINATES_SYSTEM = null;

	// ********************************************* //
	// ********** Celestial Capabilities *********** //
	// ********************************************* //
	
	@CapabilityInject(ICelestialScene.class)
	public static final Capability<ICelestialScene> CELESTIAL_SCENE = null;
	
	@CapabilityInject(ICelestialSystem.class)
	public static final Capability<ICelestialSystem> CELESTIAL_SYSTEM = null;

	// ********************************************* //
	// ********** Atmosphere Capabilities ********** //
	// ********************************************* //
	@CapabilityInject(IAtmHolder.class)
	public static final Capability<IAtmHolder> ATMOSPHERE_HOLDER = null;
	
	@CapabilityInject(IAtmSystem.class)
	public static final Capability<IAtmSystem> ATMOSPHERE_SYSTEM = null;

	//@CapabilityInject(IOpticalViewer.class)
	//public static final Capability<IOpticalViewer> VIEWER_CAPABILITY = null;
	
	//@CapabilityInject(IOpticalProperties.class)
	//public static final Capability<IOpticalProperties> OPTICAL_PROPERTY = null;
}